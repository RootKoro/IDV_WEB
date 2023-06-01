import React from 'react';
import {
  MDBContainer,
  MDBBtn,
  MDBInput,
  MDBTabs,
  MDBTabsContent,
  MDBTabsItem,
  MDBTabsLink,
  MDBTypography
}
from 'mdb-react-ui-kit';

import { AuthService } from '../services/auth.service';

export class Auth extends React.Component {
    state = { }
    authService = new AuthService();

    constructor() {
        super();

        this.state = {
            login: true,
            loggedIn: false,
            username: "",
            password: "",
            error: "",
            success: "",
        };
    }

    isLoggedIn = () => {
        let cookies = document.cookie.split(';');

        cookies.forEach(cookie => {
            if (cookie.includes('token'))
                this.setState({ loggedIn: true });
        })
    }

    switchForm = val => {
        if (val === this.state.login)
            return;
        
        this.setState({
            login: !this.state.login,
        });
    }

    authHandler = () => {
        let data = JSON.stringify({
            username: this.state.username,
            password: this.state.password,
        });

        if (this.state.login) {
            this.authService.authenticate(data)
            .then(response => {
                if (response.status !== 200)
                    this.setState({error: "Wrong username or password"});
                else
                    return response.json();
            })
            .then(result => {
                if (result){
                    document.cookie = "token=" + result.token;
                    this.setState({error: "", loggedIn: true});
                }
            })
            .catch(error => console.error(error));
        } else {
            this.authService.register(data)
            .then(response => {
                if (response.status !== 201)
                    this.setState({success: "", error: "Try with another username and password (they're both required)"});
                else
                    this.setState({error: "", success: "Registered successfully! You may now login!"});
            })
            .catch(error => console.error(error));
        }
    }

    componentDidMount = () => this.isLoggedIn()

    componentDidUpdate = () => {}

    render() {
        if (this.state.loggedIn)
            window.location = "http://localhost:3000/home";
        
        return(
            <div>
                <MDBContainer className="p-3 my-5 d-flex flex-column w-50">
                    <MDBTabs pills justify className='mb-3 d-flex flex-row justify-content-between'>
                        <MDBTabsItem>
                            <MDBTabsLink onClick={() => this.switchForm(true)} active={this.state.login}>
                                Login
                            </MDBTabsLink>
                        </MDBTabsItem>
                        <MDBTabsItem>
                            <MDBTabsLink onClick={() => this.switchForm(false)} active={!this.state.login}>
                                Register
                            </MDBTabsLink>
                        </MDBTabsItem>
                    </MDBTabs>

                    <MDBTabsContent>
                        <MDBInput wrapperClass='mb-4' label='username' id='username' type='text' onChange={e => this.setState({username: e.target.value})} />
                        <MDBInput wrapperClass='mb-4' label='password' id='password' type='password' onChange={e => this.setState({password: e.target.value})}/>

                        <MDBTypography tag='small' id='error' className='text-danger'> { this.state.error } </MDBTypography>
                        <MDBTypography tag='small' id='success' className='text-success'> { this.state.success } </MDBTypography>

                        <MDBBtn className="mb-4 w-100" onClick={() => this.authHandler()}>{this.state.login ? 'Log in' : 'Register'}</MDBBtn>
                    </MDBTabsContent>
                </MDBContainer>
            </div>
        )
    }
}