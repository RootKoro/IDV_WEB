import React, { Component }  from 'react';

import SideBar from '../components/sideBar';
import { SelfForm } from '../components/userForm';

export class Profile extends Component {
    state = {  }

    constructor() {
        super();
        this.state = {
            loggedIn: true,
            error: "",
            success: "",
        };
    }

    isLoggedIn = () => {
        let cookies = document.cookie.split(';');
        this.setState({ loggedIn: false });

        cookies.forEach(cookie => {
            if (cookie.includes('token'))
                this.setState({ loggedIn: true });
        })
    }

    componentDidMount = () => this.isLoggedIn()

    componentDidUpdate = () => {}

    render() {
        if (!this.state.loggedIn)
            window.location = "http://localhost:3000/"
        
        return (
            <div>
                <SideBar />
                <SelfForm />
            </div>
        );
    }
}