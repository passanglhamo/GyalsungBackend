import React, { useState, useMemo, useEffect } from "react";
import { useSelector } from "react-redux";
import Button from '@material-ui/core/Button';
import { makeStyles } from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import FormHelperText from '@material-ui/core/FormHelperText';
import { useNavigate } from 'react-router-dom';
import Typography from '@material-ui/core/Typography';
import ChevronLeftRoundedIcon from '@material-ui/icons/ChevronLeftRounded';
import EmailOutlinedIcon from '@material-ui/icons/EmailOutlined';
import ChevronRightRoundedIcon from '@material-ui/icons/ChevronRightRounded';
import { ArrowBack, ArrowForward, CheckCircle } from '@material-ui/icons';

import profileService from "../../services/profile.service";


const useStyles = makeStyles((theme) => ({
    root: {
        '& .MuiFormControl-root': {
            width: '100%',
            margin: theme.spacing(1)
        }
    },
    formControl: {
        margin: theme.spacing(1),
        minWidth: '100%',
    },

    generalSetting: {
        width: '100%',
        maxWidth: 500,
    },
}));


const EditEmailAddress = () => {
    const classes = useStyles();
    const navigate = useNavigate();
    const { user: currentUser } = useSelector((state) => state.auth);
    const [email, setEmail] = useState('');
    const [vcode, setVcode] = useState('');
    const [showEmailErrorMsg, setShowEmailErrorMsg] = useState(false);
    const [emailErrorMsg, setEmailErrorMsg] = useState('');
    const [vcodeErrorMsg, setVcodeErrorMsg] = useState(false);
    const [errorMsg, setErrorMsg] = useState(false);
    const [successfull, setSuccessfull] = useState(false);
    const [activeStep, setActiveStep] = useState(0);
    const [userInfo, setUserInfo] = useState([]);

    let userId = currentUser.userId;

    useEffect(() => {
        getProfileInfo();
    }, []);

    const getProfileInfo = () => {
        profileService.getProfileInfo(userId).then(
            response => {
                setUserInfo(response.data);
            },
            error => {
                console.log(
                    (error.response && error.response.data && error.response.data.message) ||
                    error.message || error.toString()
                );
            }
        );
    };

    const handleEmailChange = (e) => {
        setEmail(e.target.value);
        if (e.target.value.length > 3) {
            setShowEmailErrorMsg(false);
        } else {
            setShowEmailErrorMsg(true);
            setEmailErrorMsg('Email is required.');
        }
    };

    const handleVcodeChange = (e) => {
        setVcode(e.target.value);
    };

    const handleNext = (e) => {
        if (email.length > 3) {
            setShowEmailErrorMsg(false);
            profileService.checkEmailExistOrNot(email).then(
                response => {
                    sendVcode();
                    setActiveStep((prevActiveStep) => prevActiveStep + 1);
                    setShowEmailErrorMsg(false);
                },
                error => {
                    setShowEmailErrorMsg(true);
                    setEmailErrorMsg(error.response.data.message);
                }
            );
        } else {
            setShowEmailErrorMsg(true);
            setEmailErrorMsg('Email is required.');
        }
    }

    const sendVcode = (e) => {
        const data = { userId, email };
        profileService.receiveEmailVcode(data).then(
            response => {
                setVcodeErrorMsg(false);
            },
            error => {
                setVcodeErrorMsg(true);//to show incorrect otp error msg  
            }
        );
    }

    const handleSubmit = (e) => {
        let verificationCode = vcode;
        const data = { userId, email, verificationCode };
        profileService.changeEmail(data).then(
            response => {
                //show success message
                setVcodeErrorMsg(false);
                setSuccessfull(true);
            },
            error => {
                //show error message 
                setVcodeErrorMsg(true);
                setErrorMsg(error.response.data.message);
            }
        );
    }

    return (
        <>
            <div className="col-md-12 row mb-2">
                <div className="col-md-4">
                    <Button variant="contained" color="primary" size="small" onClick={() => navigate("/gyalsupProfile")}>
                        <ChevronLeftRoundedIcon /> Go back </Button>
                </div>
            </div>
            <div className="col-md-12 row">
                <div className="d-flex flex-wrap flex-column align-items-center justify-content-center">
                    <div className="col-md-10 text-muted">
                        <div className="cards">
                            <div className="card-bodys">
                                <div className="col-md-12 row mb-2">
                                    <div className="d-flex flex-wrap flex-column align-items-center justify-content-center">
                                        <EmailOutlinedIcon style={{ fontSize: 60 }} />
                                        <Typography component="h1" variant="h6" color="inherit" noWrap>
                                            Change email address
                                        </Typography>
                                    </div>
                                </div>
                                {successfull === true ? (
                                    <div className='text-center alert alert-success text-success animated bounceIn'>
                                        <h5><CheckCircle /> Confirmation!</h5>
                                        <strong> Your email has been changed successfully.</strong>
                                    </div>
                                ) : (
                                    <form className={classes.root}>
                                        <span> To change email address you need to verify it belong to you through verification code.
                                            Verification code will be sent to your email.</span>
                                        <div className='col-md-12'>
                                            Your current email address is: {userInfo.email}
                                        </div>
                                        <div className='col-md-12 row mb-2'>
                                            <div className="d-flex flex-wrap flex-column align-items-center justify-content-center">

                                                {activeStep === 0 ?
                                                    <div className='col-md-6'>
                                                        <TextField id="emailAddress" label="New email address" required
                                                            value={email}
                                                            error={showEmailErrorMsg}
                                                            helperText={showEmailErrorMsg === true ? emailErrorMsg : ''}
                                                            onChange={handleEmailChange} />
                                                    </div>
                                                    :
                                                    <div className='col-md-6'>
                                                        <strong>New Email: {email}</strong>
                                                        <TextField id="verificarionCode" label="Verification code"
                                                            value={vcode}
                                                            error={vcodeErrorMsg}
                                                            helperText={vcodeErrorMsg === true ? errorMsg : ''}
                                                            required onChange={handleVcodeChange}
                                                        />
                                                        <div className="col-md-12 row">
                                                            <div className="col">
                                                                Enter 6-digit verificaiton code sent to your email.
                                                                Didn't receive email?
                                                                <Button variant="outline" color="primary"
                                                                // onClick={sendVerificationCode}
                                                                >
                                                                    Resend
                                                                </Button>
                                                            </div>
                                                        </div>
                                                    </div>}
                                            </div>
                                        </div>
                                        <div className='col-md-12 row'>
                                            <div className='d-flex flex-wrap flex-column align-items-center'>
                                                {activeStep === 0 ?
                                                    <Button variant="contained" color="primary" onClick={handleNext}>
                                                        Next <ChevronRightRoundedIcon />
                                                    </Button> :
                                                    <Button variant="contained" color="primary" onClick={handleSubmit}>
                                                        Submit
                                                    </Button>}
                                            </div>
                                        </div>
                                    </form>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            </div >
        </>
    )
}

export default EditEmailAddress;