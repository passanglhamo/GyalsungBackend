import React, { useState } from 'react';

const SignupVerification = (props) => {

    const handleToggle = () => {
        props.setAgreeTerms(!props.agreeTerms);
    };

    return (
        <div className='text-muted col-md-12 p-4'>
            <div className='d-flex flex-wrap flex-column align-items-center justify-content-center'>
                <div className='col-md-7'>
                    <p><strong>Please verify following:</strong></p>
                    <table className='table-responsive'>
                        <tr>
                            <td>Name:</td>
                            <td><strong>{props.data.fullName}</strong></td>
                        </tr>
                        <tr>
                            <td>CID:</td>
                            <td><strong>{props.data.cid}</strong></td>
                        </tr>
                        <tr>
                            <td>DOB:</td>
                            <td><strong>{props.data.dob}</strong></td>
                        </tr>
                        <tr>
                            <td>Mobile number:</td>
                            <td><strong>{props.data.mobileNo}</strong></td>
                        </tr>
                        <tr>
                            <td>Email:</td>
                            <td><strong>{props.data.email}</strong></td>
                        </tr>
                        <tr>
                            <td>Gender:</td>
                            <td><strong>{props.data.gender}</strong></td>
                        </tr>
                        <tr>
                            <td>Father's name:</td>
                            <td><strong>{props.data.fatherName}</strong></td>
                        </tr>
                        <tr>
                            <td>Father's CID:</td>
                            <td><strong>{props.data.fatherCid}</strong></td>
                        </tr>
                        <tr>
                            <td>Mother's name:</td>
                            <td><strong>{props.data.motherName}</strong></td>
                        </tr>
                        <tr>
                            <td>Mother's CID:</td>
                            <td><strong>{props.data.motherCid}</strong></td>
                        </tr>
                        <tr>
                            <td>Village:</td>
                            <td><strong>{props.data.permanentPlaceName}</strong></td>
                        </tr>
                        <tr>
                            <td>Geog:</td>
                            <td><strong>{props.data.permanentGeog}</strong></td>
                        </tr>
                        <tr>
                            <td>Dzongkhag:</td>
                            <td className='ml-4'><strong>{props.data.permanentDzongkhag}</strong></td>
                        </tr>
                    </table>
                    <div className='col-md-12'>
                        <label onClick={handleToggle}>
                            <input type="checkbox" /> <strong> I declare that the information provided above is
                                accurate and true to the best of my knowledge.
                            </strong>
                        </label>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default SignupVerification;