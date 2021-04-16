import { LoggingSettings } from "./logging-settings";
export declare function configure(successCallback: () => void, errorCallback: (error: any) => void, settings: LoggingSettings): void;
export declare function log(successCallback: () => void, errorCallback: (error: any) => void, logLevel: 'debug' | 'info' | 'warn' | 'error', message: string): void;
export declare function debug(successCallback: () => void, errorCallback: (error: any) => void, message: string): void;
export declare function info(successCallback: () => void, errorCallback: (error: any) => void, message: string): void;
export declare function warn(successCallback: () => void, errorCallback: (error: any) => void, message: string): void;
export declare function error(successCallback: () => void, errorCallback: (error: any) => void, message: string): void;
export interface LoggingLocal {
    configure(successCallback: () => void, errorCallback: (error: any) => void, settings: LoggingSettings): void;
    log(successCallback: () => void, errorCallback: (error: any) => void, logLevel: 'debug' | 'info' | 'warn' | 'error', message: string): void;
    debug(successCallback: () => void, errorCallback: (error: any) => void, message: string): void;
    info(successCallback: () => void, errorCallback: (error: any) => void, message: string): void;
    warn(successCallback: () => void, errorCallback: (error: any) => void, message: string): void;
    error(successCallback: () => void, errorCallback: (error: any) => void, message: string): void;
}
