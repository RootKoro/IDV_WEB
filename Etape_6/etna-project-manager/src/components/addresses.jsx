import React, { useState } from 'react';
import {
    MDBContainer,
    MDBRow,
    MDBCol,
    MDBTabs,
    MDBTabsItem,
    MDBTable,
    MDBTableHead,
    MDBTableBody,
    MDBBtn,
    MDBTabsLink,
    MDBTabsContent,
    MDBTabsPane,
    MDBInput,
    MDBTypography
} from 'mdb-react-ui-kit';

import { AddressService } from '../services/address.service';
import { logout } from '../utils/logout';

export function Addresses() {
    const addressService = new AddressService();
    const [addresses, setAddresses] = useState([]);
    
    const getAddresses = () => {
        addressService.index()
        .then(response => {
            if (response.status !== 200)
                logout();
            return response.json();
        }).then(result => setAddresses(result))
        .catch(error => console.error(error));
    }

    getAddresses();

    return ( 
        <MDBContainer>
            <MDBRow className="mb-3">
                <AddressFrom />
            </MDBRow>

            <MDBRow>
                <MDBContainer className="mb-3 d-flex flex-row justify-content-center">
                    <MDBTypography tag='h2'>LIST OF THE ADDRRESSES</MDBTypography>
                </MDBContainer>
            </MDBRow>

            <MDBRow className='mb-3'>
                <MDBCol size='3'></MDBCol>
                <MDBCol size='9'>
                    <MDBTable striped hover>
                        <MDBTableHead>
                            <tr>
                                <th scope='col'>#</th>
                                <th scope='col'>Street</th>
                                <th scope='col'>Postal Code</th>
                                <th scope='col'>City</th>
                                <th scope='col'>Country</th>
                                <th scope='col'>Owner</th>
                                <th scope='col'>DELETE</th>
                            </tr>
                        </MDBTableHead>
                        <MDBTableBody>
                            {addresses.map((address, index) => (<Address item={address} key={index} />))}
                        </MDBTableBody>
                    </MDBTable>
                </MDBCol>
            </MDBRow>
        </MDBContainer>
     );
}

export function Address(props){
    let addressService = new AddressService();

    const delAddress = (id) => {
        addressService.destroy(id)
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
            <td> { props.item.street } </td>
            <td> { props.item.postalCode } </td>
            <td> { props.item.city } </td>
            <td> { props.item.country } </td>
            <td> { props.item.owner } </td>
            <td> <MDBBtn color='danger' onClick={() => delAddress(props.item.id)}>DEL</MDBBtn> </td>
        </tr>
    )
}

export function AddressFrom() {
    const [mode, setMode] = useState(true);
    const [id, setId] = useState('');
    const [street, setStreet] = useState('');
    const [postalCode, setPostalCode] = useState('');
    const [city, setCity] = useState('');
    const [country, setCountry] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const addressService = new AddressService();

    const switchForm = val => {
        if (val === mode)
            return;
        
        setMode(!mode);
    }

    const formHandler = () => {
        let data = JSON.stringify({
            street: street,
            postalCode: postalCode,
            city: city,
            country: country
        });

        if (mode) {
            addressService.store(data)
            .then(response => {
                if (response.status !== 201){
                    setSuccess("");
                    setError("All fields are required!");
                }
                else{
                    setError("");
                    setSuccess("Address successfully created!");
                }
            }).catch(error => console.error(error));
        } else {
            addressService.update(id, data)
            .then(response => {
                if (response.status !== 200){
                    setSuccess("");
                    setError("All fields are required!");
                }
                else{
                    setError("");
                    setSuccess("Address successfully updated!");
                }
            }).catch(error => console.error(error));
        }
    }

    return (
        <MDBContainer className="p-3 my-5 d-flex flex-column w-50">
            <div className='mb-3 d-flex flex-row justify-content-center'>
                <MDBTypography tag='h1'>CREATE / UPDATE AN ADDRESS</MDBTypography>
            </div>
            <MDBTabs pills justify className='mb-3 d-flex flex-column justify-content-between'>
                <MDBTabsItem>
                    <MDBTabsLink onClick={() => switchForm(true)} active={mode}>
                        CREATE A NEW ADDRESS
                    </MDBTabsLink>
                </MDBTabsItem>
                <MDBTabsItem>
                    <MDBTabsLink onClick={() => switchForm(false)} active={!mode}>
                        UPDATE AN ADDRESS
                    </MDBTabsLink>
                </MDBTabsItem>

                <MDBTabsContent>
                    <MDBTabsPane show={mode}>
                        <MDBInput wrapperClass='mb-4' label='street' id='street' type='text' onChange={e => setStreet(e.target.value)} />
                        <MDBInput wrapperClass='mb-4' label='postal-code' id='postal-code' type='text' onChange={e => setPostalCode(e.target.value)}/>
                        <MDBInput wrapperClass='mb-4' label='city' id='city' type='text' onChange={e => setCity(e.target.value)} />
                        <MDBInput wrapperClass='mb-4' label='country' id='country' type='text' onChange={e => setCountry(e.target.value)} />

                        <MDBTypography tag='small' id='error' className='text-danger'> { error } </MDBTypography>
                        <MDBTypography tag='small' id='success' className='text-success'> { success } </MDBTypography>

                        <MDBBtn className="mb-4 w-100" onClick={() => formHandler()}> --- CREATE --- </MDBBtn>
                    </MDBTabsPane>
                    
                    <MDBTabsPane show={!mode}>
                        <MDBInput wrapperClass='mb-4' label='address ID' id='address-id' type='number' onChange={e => setId(e.target.value)} />
                        <MDBInput wrapperClass='mb-4' label='street' id='street' type='text' onChange={e => setStreet(e.target.value)} />
                        <MDBInput wrapperClass='mb-4' label='postal-code' id='postal-code' type='text' onChange={e => setPostalCode(e.target.value)}/>
                        <MDBInput wrapperClass='mb-4' label='city' id='city' type='text' onChange={e => setCity(e.target.value)} />
                        <MDBInput wrapperClass='mb-4' label='country' id='country' type='text' onChange={e => setCountry(e.target.value)} />

                        <MDBTypography tag='small' id='error' className='text-danger'> { error } </MDBTypography>
                        <MDBTypography tag='small' id='success' className='text-success'> { success } </MDBTypography>

                        <MDBBtn className="mb-4 w-100" onClick={() => formHandler()}> --- UPDATE --- </MDBBtn>
                    </MDBTabsPane>
                </MDBTabsContent>
            </MDBTabs>
        </MDBContainer>
    );
}