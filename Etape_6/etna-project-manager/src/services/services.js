export class Services{
    headers= new Headers();
    cookies = [];

    constructor () {
        this.headers.append('Content-Type', 'application/json');
        this.cookies = document.cookie.split(';');
    }
}