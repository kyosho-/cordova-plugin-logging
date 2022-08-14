export class LoggingSettings {
  level?: 'ALL' | 'TRACE' | 'DEBUG' | 'INFO' | 'WARN' | 'ERROR' | 'OFF';
  logFilePath: string;
  layoutPattern: string;
  maxFileSize: string;
  rollingFileNamePostfix: string;
  maxHistory: number;
}
