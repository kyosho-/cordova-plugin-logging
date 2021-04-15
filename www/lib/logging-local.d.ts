import { CallbackResult } from "./callback-result";
export declare function log(successCallback: (success: CallbackResult<void>) => void, errorCallback: (error: CallbackResult<any>) => void, logLevel: 'debug' | 'info' | 'warn' | 'error', message: string): void;
export declare function debug(successCallback: (success: CallbackResult<void>) => void, errorCallback: (error: CallbackResult<any>) => void, message: string): void;
export declare function info(successCallback: (success: CallbackResult<void>) => void, errorCallback: (error: CallbackResult<any>) => void, message: string): void;
export declare function warn(successCallback: (success: CallbackResult<void>) => void, errorCallback: (error: CallbackResult<any>) => void, message: string): void;
export declare function error(successCallback: (success: CallbackResult<void>) => void, errorCallback: (error: CallbackResult<any>) => void, message: string): void;
export interface LoggingLocal {
    log(successCallback: (success: CallbackResult<void>) => void, errorCallback: (error: CallbackResult<any>) => void, logLevel: 'debug' | 'info' | 'warn' | 'error', message: string): void;
    debug(successCallback: (success: CallbackResult<void>) => void, errorCallback: (error: CallbackResult<any>) => void, message: string): void;
    info(successCallback: (success: CallbackResult<void>) => void, errorCallback: (error: CallbackResult<any>) => void, message: string): void;
    warn(successCallback: (success: CallbackResult<void>) => void, errorCallback: (error: CallbackResult<any>) => void, message: string): void;
    error(successCallback: (success: CallbackResult<void>) => void, errorCallback: (error: CallbackResult<any>) => void, message: string): void;
}
