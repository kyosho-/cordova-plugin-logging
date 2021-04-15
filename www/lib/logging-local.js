"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.error = exports.warn = exports.info = exports.debug = exports.log = void 0;
/**
 * @hidden
 * Related Java Class Name.
 */
var CLASS_NAME = 'LoggingLocal';
function log(successCallback, errorCallback, logLevel, message) {
    var methodName = 'log';
    cordova.exec(successCallback, errorCallback, CLASS_NAME, methodName, [
        logLevel,
        message
    ]);
}
exports.log = log;
function debug(successCallback, errorCallback, message) {
    var logLevel = 'debug';
    log(successCallback, errorCallback, logLevel, message);
}
exports.debug = debug;
function info(successCallback, errorCallback, message) {
    var logLevel = 'info';
    log(successCallback, errorCallback, logLevel, message);
}
exports.info = info;
function warn(successCallback, errorCallback, message) {
    var logLevel = 'warn';
    log(successCallback, errorCallback, logLevel, message);
}
exports.warn = warn;
function error(successCallback, errorCallback, message) {
    var logLevel = 'error';
    log(successCallback, errorCallback, logLevel, message);
}
exports.error = error;
