import React, { useState } from 'react';
import {
    MDBBtn,
    MDBCol,
    MDBContainer,
    MDBRow,
    MDBTable,
    MDBTableBody,
    MDBTableHead,
    MDBTypography,
} from 'mdb-react-ui-kit';

import { UserService } from '../services/user.service';
import { logout } from '../utils/logout';
import { AdminForms } from './userForm';

function Users() {
    let userService = new UserService();
    const [users, setUsers] = useState([]);
    const [role, setRole] = useState('');

    const getRole = () => {
        userService.me()
        .then(response => {
            if (response.status !== 200)
                logout();
            return response.json();
        }).then(result => setRole(result.role))
        .catch(err => console.error(err));
    }

    const getUsers = () => {
        userService.index()
        .then(response => {
            if (response.status !== 200)
                logout();
            return response.json();
        })
        .then(result => setUsers(result))
        .catch(err => console.error(err));
    }

    getRole();
    getUsers();

    return (
        <MDBContainer>
            <MDBRow className="mb-3">
                {role === 'ROLE_ADMIN' 
                    ? <AdminForms />
                    : <div></div>
                }
            </MDBRow>

            <MDBRow>
                <MDBContainer className="mb-3 d-flex flex-row justify-content-center">
                    <MDBTypography tag='h2'>LIST OF THE USERS</MDBTypography>
                </MDBContainer>
            </MDBRow>
            <MDBRow className='mb-3'>
                <MDBCol size='3'></MDBCol>
                <MDBCol size='9'>
                    <MDBTable striped hover>
                        <MDBTableHead>
                            <tr>
                                <th scope='col'>#</th>
                                <th scope='col'>Username</th>
                                <th scope='col'>role</th>
                                {role === 'ROLE_ADMIN' 
                                    ? <th scope='col'>DELETE</th>
                                    : <div></div>
                                }
                            </tr>
                        </MDBTableHead>
                        <MDBTableBody>
                            {users.map((user, index) => 
                                role === 'ROLE_ADMIN'
                                    ? <AdminUser item={user} key={index} />
                                    : <User item={user} key={index} />
                            )}
                        </MDBTableBody>
                    </MDBTable>
                </MDBCol>
            </MDBRow>
        </MDBContainer>
     );
}

export default Users;

export function User(props) {
    return (
        <tr>
            <th scope='row'> { props.item.id } </th>
            <td> { props.item.username } </td>
            <td> { props.item.role } </td>
        </tr>
    )
}

export function AdminUser(props) {
    let userService = new UserService();

    const delUser = (id) => {
        userService.destroy(id)
        .then(response => {
            if (response.status !== 200)
                logout();
            return response.json();
        })
        .catch(err => console.error(err));
    }

    return (
        <tr>
            <th scope='row'> { props.item.id } </th>
            <td> { props.item.username } </td>
            <td> { props.item.role } </td>
            <td> <MDBBtn color='danger' onClick={() => delUser(props.item.id)}>DEL</MDBBtn> </td>
        </tr>
    )
}