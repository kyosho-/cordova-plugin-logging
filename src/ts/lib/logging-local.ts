/**
 * @types/cordova
 */
declare const cordova: any;

import { LoggingSettings } from "./logging-settings";

/**
 * @hidden
 * Related Java Class Name.
 */
const CLASS_NAME = 'LoggingLocal';

export function configure(
    successCallback: () => void,
    errorCallback: (error: any) => void,
    settings: LoggingSettings): void {
    const methodName = 'configure';
    cordova.exec(successCallback, errorCallback, CLASS_NAME, methodName, [
        settings
    ]);
}

export function log(
    successCallback: () => void,
    errorCallback: (error: any) => void,
    logLevel: 'debug' | 'info' | 'warn' | 'error',
    message: string): void {
    const methodName = 'log';
    cordova.exec(successCallback, errorCallback, CLASS_NAME, methodName, [
        logLevel,
        message
    ]);
}

export function debug(
    successCallback: () => void,
    errorCallback: (error: any) => void,
    message: string): void {
    const logLevel = 'debug';
    log(successCallback, errorCallback, logLevel, message);
}

export function info(
    successCallback: () => void,
    errorCallback: (error: any) => void,
    message: string): void {
    const logLevel = 'info';
    log(successCallback, errorCallback, logLevel, message);
}

export function warn(
    successCallback: () => void,
    errorCallback: (error: any) => void,
    message: string): void {
    const logLevel = 'warn';
    log(successCallback, errorCallback, logLevel, message);
}

export function error(
    successCallback: () => void,
    errorCallback: (error: any) => void,
    message: string): void {
    const logLevel = 'error';
    log(successCallback, errorCallback, logLevel, message);
}

export interface LoggingLocal {
    configure(
        successCallback: () => void,
        errorCallback: (error: any) => void,
        settings: LoggingSettings): void;

    log(successCallback: () => void,
        errorCallback: (error: any) => void,
        logLevel: 'debug' | 'info' | 'warn' | 'error',
        message: string): void;

    debug(successCallback: () => void,
        errorCallback: (error: any) => void,
        message: string): void;

    info(successCallback: () => void,
        errorCallback: (error: any) => void,
        message: string): void;

    warn(successCallback: () => void,
        errorCallback: (error: any) => void,
        message: string): void;

    error(successCallback: () => void,
        errorCallback: (error: any) => void,
        message: string): void;
}
