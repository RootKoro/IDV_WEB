import React, { Component }  from 'react';

import SideBar from '../components/sideBar';
import Users from '../components/users';

export class Dashboard extends Component {
    state = {  }

    constructor() {
        super();
        this.state = {
            loggedIn: true,
            token: "",
            error: "",
            success: "",
        };
    }

    isLoggedIn = () => {
        let cookies = document.cookie.split(';');
        this.setState({ loggedIn: false });

        cookies.forEach(cookie => {
            if (cookie.includes('token'))
                this.setState({ loggedIn: true, token: cookie.substring(6)});
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
                <Users token={this.state.token} />
            </div>
        );
    }
}
