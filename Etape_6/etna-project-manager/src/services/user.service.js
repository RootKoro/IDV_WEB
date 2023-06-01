import { Services } from "./services";

export class UserService extends Services{
    token = "";
    constructor() {
        super();
        this.cookies.forEach(cookie => {
            if (cookie.includes('token'))
                this.token += cookie.substring(6);
        })

        this.headers.append('Authorization', 'Bearer ' + this.token);
    }

    index = async () => {
        let requestOptions = {
            method: 'GET',
            headers: this.headers,
        };
        
        return await fetch('http://localhost:8090/user', requestOptions);
    }

    me = async () => {
        let requestOptions = {
            method: 'GET',
            headers: this.headers,
        };
        
        return await fetch('http://localhost:8090/me', requestOptions);
    }

    show = async (id) => {
        let requestOptions = {
            method: 'GET',
            headers: this.headers,
        };

        return await fetch('http://localhost:8090/user/' + id, requestOptions);
    }

    selfUpdate = async (data) => {
        let requestOptions = {
            method: 'PUT',
            headers: this.headers,
            body: data,
        };

        return await fetch('http://localhost:8090/user', requestOptions);
    }

    update = async (id, data) => {
        let requestOptions = {
            method: 'PUT',
            headers: this.headers,
            body: data,
        };

        return await fetch('http://localhost:8090/user/' + id, requestOptions);
    }

    selfDestroy = async () => {
        let requestOptions = {
            method: 'DELETE',
            headers: this.headers,
        };

        return await fetch('http://localhost:8090/user/', requestOptions);
    }

    destroy = async (id) => {
        let requestOptions = {
            method: 'DELETE',
            headers: this.headers,
        };

        return await fetch('http://localhost:8090/user/' + id, requestOptions);
    }
}