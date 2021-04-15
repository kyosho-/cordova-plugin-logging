export interface CallbackResult<T> {
    type: 'next' | 'complete' | 'error';
    name: string;
    message?: string;
    data?: T;
}
