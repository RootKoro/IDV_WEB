import { Services } from "./services";

export class AddressService extends Services{
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
        
        return await fetch('http://localhost:8090/address', requestOptions);
    }

    show = async (id) => {
        let requestOptions = {
            method: 'GET',
            headers: this.headers,
        };

        return await fetch('http://localhost:8090/address/' + id, requestOptions);
    }

    store = async (data) => {
        let requestOptions = {
            method: 'POST',
            headers: this.headers,
            body: data,
        };

        return await fetch('http://localhost:8090/address', requestOptions);
    }

    update = async (id, data) => {
        let requestOptions = {
            method: 'PUT',
            headers: this.headers,
            body: data,
        };

        return await fetch('http://localhost:8090/address/' + id, requestOptions);
    }

    destroy = async (id) => {
        let requestOptions = {
            method: 'DELETE',
            headers: this.headers,
        };

        return await fetch('http://localhost:8090/address/' + id, requestOptions);
    }
}