const fs = require('fs-extra');
const fsPromises = fs.promises;
const path = require('path');

/**
 * テストソースパス
 */
var srcBasePath = 'src/android/test';

/**
 * 配置先パス
 */
var dstBasePath = 'platforms/android/app/src/test'

const readdirRecursively = async (dir, files = []) => {
    const dirents = await fsPromises.readdir(dir, { withFileTypes: true });
    const dirs = [];
    for (const dirent of dirents) {
        if (dirent.isDirectory()) dirs.push(`${dir}/${dirent.name}`);
        if (dirent.isFile()) files.push(`${dir}/${dirent.name}`);
    }
    for (const d of dirs) {
        files = await readdirRecursively(d, files);
    }
    return Promise.resolve(files);
};

module.exports = function (context) {
    const srcPath = path.normalize(path.join(context.opts.plugin.dir, srcBasePath));
    const dstPath = path.normalize(path.join(context.opts.projectRoot, dstBasePath));

    (async () => {
        const result = await readdirRecursively(srcPath).catch(err => {
            console.error("Error:", err);
        });
        result.forEach(filePath => {
            const keyPath = path.relative(srcPath, filePath);
            const srcFile = path.normalize(path.join(srcPath, keyPath));
            const dstFile = path.normalize(path.join(dstPath, keyPath));
            const dstDir = path.dirname(dstFile);

            fs.mkdirsSync(dstDir);
            if (fs.existsSync(srcFile) && fs.existsSync(dstDir)) {
                fs.createReadStream(srcFile).pipe(fs.createWriteStream(dstFile));
            }
        });
    })();
};