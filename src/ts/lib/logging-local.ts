/**
 * @types/cordova
 */
declare const cordova: any;

import { CallbackResult } from "./callback-result";

/**
 * @hidden
 * Related Java Class Name.
 */
const CLASS_NAME = 'LoggingLocal';

export function log(
    successCallback: (success: CallbackResult<void>) => void,
    errorCallback: (error: CallbackResult<any>) => void,
    logLevel: 'debug' | 'info' | 'warn' | 'error',
    message: string): void {
    const methodName = 'log';
    cordova.exec(successCallback, errorCallback, CLASS_NAME, methodName, [
        logLevel,
        message
    ]);
}

export function debug(
    successCallback: (success: CallbackResult<void>) => void,
    errorCallback: (error: CallbackResult<any>) => void,
    message: string): void {
    const logLevel = 'debug';
    log(successCallback,
        errorCallback,
        logLevel,
        message);
}

export function info(
    successCallback: (success: CallbackResult<void>) => void,
    errorCallback: (error: CallbackResult<any>) => void,
    message: string): void {
    const logLevel = 'info';
    log(successCallback,
        errorCallback,
        logLevel,
        message);
}

export function warn(
    successCallback: (success: CallbackResult<void>) => void,
    errorCallback: (error: CallbackResult<any>) => void,
    message: string): void {
    const logLevel = 'warn';
    log(successCallback,
        errorCallback,
        logLevel,
        message);
}

export function error(
    successCallback: (success: CallbackResult<void>) => void,
    errorCallback: (error: CallbackResult<any>) => void,
    message: string): void {
    const logLevel = 'error';
    log(successCallback,
        errorCallback,
        logLevel,
        message);
}

export interface LoggingLocal {
    log(successCallback: (success: CallbackResult<void>) => void,
        errorCallback: (error: CallbackResult<any>) => void,
        logLevel: 'debug' | 'info' | 'warn' | 'error',
        message: string): void;

    debug(successCallback: (success: CallbackResult<void>) => void,
        errorCallback: (error: CallbackResult<any>) => void,
        message: string): void;

    info(successCallback: (success: CallbackResult<void>) => void,
        errorCallback: (error: CallbackResult<any>) => void,
        message: string): void;

    warn(successCallback: (success: CallbackResult<void>) => void,
        errorCallback: (error: CallbackResult<any>) => void,
        message: string): void;

    error(successCallback: (success: CallbackResult<void>) => void,
        errorCallback: (error: CallbackResult<any>) => void,
        message: string): void;
}
