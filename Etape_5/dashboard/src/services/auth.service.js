import { Services } from "./services";

export class AuthService extends Services{
    register = async (data) => {
        let requestOptions = {
            method: 'POST',
            headers: this.headers,
            body: data,
        };
        
        return await fetch('http://localhost:8090/register', requestOptions);
    }

    authenticate = async (data) => {
        let requestOptions = {
            method: 'POST',
            headers: this.headers,
            body: data,
        };

        return await fetch('http://localhost:8090/authenticate', requestOptions);
    }
}