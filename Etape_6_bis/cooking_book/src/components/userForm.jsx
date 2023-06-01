import React, { useState } from 'react';
import { MDBContainer, MDBTabs, MDBTabsItem, MDBBtn, MDBTabsLink, MDBTabsContent, MDBTabsPane, MDBInput, MDBTypography } from 'mdb-react-ui-kit';

import { AuthService } from '../services/auth.service';
import { UserService } from '../services/user.service';
import { logout } from '../utils/logout';

export function AdminForms() {
    const [mode, setMode] = useState(true);
    const [id, setId] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const authService = new AuthService();
    const userService = new UserService();

    const switchForm = val => {
        if (val === mode)
            return;
        
        setMode(!mode);
    }

    const formHandler = () => {
        let data = JSON.stringify({
            username: username,
            password: password,
        });

        if (mode) {
            authService.register(data)
            .then(response => {
                if (response.status !== 201){
                    setSuccess("");
                    setError("Try with another username (both fields are required)");
                }
                else{
                    setError("");
                    setSuccess("A new user have been successfully registered.");
                }
            }).catch(error => console.error(error));
        } else {
            userService.update(id, data)
            .then(response => {
                if (response.status !== 200){
                    setSuccess("");
                    setError("Try with another username (both fields are required)");
                }
                else{
                    setError("");
                    setSuccess("The user have been successfully updated.");
                }
            }).catch(error => console.error(error));
        }
    }
    
    return (
        <MDBContainer className="p-3 my-5 d-flex flex-column w-50">
            <div className='mb-3 d-flex flex-row justify-content-center'>
                <MDBTypography tag='h1'>CREATE / UPDATE A USER</MDBTypography>
            </div>
            <MDBTabs pills justify className='mb-3 d-flex flex-column justify-content-between'>
                <MDBTabsItem>
                    <MDBTabsLink onClick={() => switchForm(true)} active={mode}>
                        CREATE A USER
                    </MDBTabsLink>
                </MDBTabsItem>
                <MDBTabsItem>
                    <MDBTabsLink onClick={() => switchForm(false)} active={!mode}>
                        UPDATE A USER
                    </MDBTabsLink>
                </MDBTabsItem>

                <MDBTabsContent>
                    <MDBTabsPane show={mode}>
                        <MDBInput wrapperClass='mb-4' label='username' id='username' type='text' onChange={e => setUsername(e.target.value)} />
                        <MDBInput wrapperClass='mb-4' label='password' id='password' type='password' onChange={e => setPassword(e.target.value)}/>

                        <MDBTypography tag='small' id='error' className='text-danger'> { error } </MDBTypography>
                        <MDBTypography tag='small' id='success' className='text-success'> { success } </MDBTypography>

                        <MDBBtn className="mb-4 w-100" onClick={() => formHandler()}> --- CREATE --- </MDBBtn>
                    </MDBTabsPane>
                    
                    <MDBTabsPane show={!mode}>
                        <MDBInput wrapperClass='mb-4' label='user-id' id='user-id' type='number' onChange={e => setId(e.target.value)} />
                        <MDBInput wrapperClass='mb-4' label='username' id='username' type='text' onChange={e => setUsername(e.target.value)} />

                        <MDBTypography tag='small' id='error' className='text-danger'> { error } </MDBTypography>
                        <MDBTypography tag='small' id='success' className='text-success'> { success } </MDBTypography>

                        <MDBBtn className="mb-4 w-100" onClick={() => formHandler()}> --- UPDATE --- </MDBBtn>
                    </MDBTabsPane>
                </MDBTabsContent>
            </MDBTabs>
        </MDBContainer>
    );
}

export function SelfForm() {
    const [username, setUsername] = useState('');
    const [role, setRole] = useState('ROLE_USER');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const userService = new UserService();

    userService.me()
    .then(response => {
        if (response.status !== 200)
            logout();
        return response.json();
    }).then(result => setRole(result.role))
    .catch(error => console.error(error));

    const formHandler = () => {
        let data = JSON.stringify({ username: username });
        
        userService.selfUpdate(data)
        .then(response => {
            if (response.status !== 200){
                setSuccess("");
                setError("Try with another username (The field is required).");
            }
            else{
                setError("");
                setSuccess("Your profile has successfully been updated.");
            }
        }).catch(error => console.error(error));
    }

    return (
        <MDBContainer className="p-3 my-5 d-flex flex-column w-50">
            <div className='mb-3 d-flex flex-row justify-content-center'>
                <MDBTypography tag='h1'>UPDATE YOUR PROFILE</MDBTypography>
            </div>
            
            <MDBTabs pills justify className='mb-3 d-flex flex-column justify-content-between'>
                <MDBTabsContent>
                    <MDBInput label={role} id='role' className='mb-2' type='text' disabled />
                    <MDBInput wrapperClass='mb-4' label='username' id='username' type='text' onChange={e => setUsername(e.target.value)} />

                    <MDBTypography tag='small' id='error' className='text-danger'> { error } </MDBTypography>
                    <MDBTypography tag='small' id='success' className='text-success'> { success } </MDBTypography>

                    <MDBBtn className="mb-4 w-100" onClick={() => formHandler()}> --- UPDATE --- </MDBBtn>
                </MDBTabsContent>
            </MDBTabs>
        </MDBContainer>
    );
}