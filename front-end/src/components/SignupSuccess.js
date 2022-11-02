import React from 'react';
import { useNavigate } from 'react-router-dom';
import Button from '@material-ui/core/Button';
import { ArrowBack, ArrowForward, CheckCircle } from '@material-ui/icons';
import moment from 'moment';

const SignupSuccess = (props) => {
    const navigate = useNavigate();

    function getCurrentDate() {
        return moment(new Date()).format('MMM DD, YYYY');
    };

    function getSalutation() {
        let salutation = 'Mr.'
        let gender = props.data.gender;
        if (gender === 'Female') {
            salutation = 'Ms.'
        }
        return salutation;
    };

    return (
        <div className=''>
            <br></br>
            <div className='card'>
                <div className='card-body'>
                    <div className='alert alert-successee'>
                        <div className='text-center alert alert-success text-success'>
                            <div className='s'>
                                <strong className='h5'><CheckCircle /> Confirmation!</strong>
                                <strong>You have successfully registered to Gyalsung System.</strong>
                            </div>
                        </div>
                        <div className=''>
                            <p>{getSalutation()} {props.data.fullName},</p>
                            <p>Thank you for registering to Gyalsung System on {getCurrentDate()}. Please login to the
                                system using email/cid and password you have provided. You are required to update
                                profile which includes family detail, contact detail, social media links and education detail.
                            </p>
                            <p>
                                Please enrol for Enlishment if you have completed class twlve or attained 18 years old.
                            </p>
                        </div>
                        <div className='dropdown-divider'></div>
                        <div className='col-md-12'>
                            <div className='d-flex justify-content-center pt-4'>
                                <Button variant="contained" color="primary" onClick={() => navigate("/")}>
                                    <ArrowBack />  Back to home page
                                </Button>
                                &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
                                <Button variant="contained" color="primary" onClick={() => navigate("/signin")}>
                                    Okay, Go to login <ArrowForward />
                                </Button>
                            </div>
                        </div>
                    </div>
                </div>
            </div></div>
    )
}

export default SignupSuccess