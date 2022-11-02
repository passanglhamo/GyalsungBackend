import React, { useState, useMemo, useEffect } from "react";
import { useSelector } from "react-redux";
import Button from '@material-ui/core/Button';
import { makeStyles } from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import FormHelperText from '@material-ui/core/FormHelperText';
import { useNavigate } from 'react-router-dom';
import Typography from '@material-ui/core/Typography';
import ChevronLeftRoundedIcon from '@material-ui/icons/ChevronLeftRounded';
import PhoneAndroidIcon from '@material-ui/icons/PhoneAndroid';
import ChevronRightRoundedIcon from '@material-ui/icons/ChevronRightRounded';
import profileService from "../../services/profile.service";
import { ArrowBack, ArrowForward, CheckCircle } from '@material-ui/icons';



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


const EditMobileNo = () => {
    const classes = useStyles();
    const navigate = useNavigate();
    const { user: currentUser } = useSelector((state) => state.auth);
    const [mobileNo, setMobileNo] = useState('');
    const [otp, setOtp] = useState('');
    const [activeStep, setActiveStep] = useState(0);
    const [mobileNoErrorMsg, setMobileNoErrorMsg] = useState(false);
    const [otpErrorMsg, setOtpErrorMsg] = useState(false);
    const [successfull, setSuccessfull] = useState(false);
    const [errorMsg, setErrorMsg] = useState('');
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

    const handleMobileNoChange = (e) => {
        setMobileNo(e.target.value);
        if (e.target.value.length > 6) {
            setMobileNoErrorMsg(false);
        } else {
            setMobileNoErrorMsg(true);
        }
    };

    const handleOtpChange = (e) => {
        setOtp(e.target.value);
    };

    const handleNext = (e) => {
        if (mobileNo.length > 6) {
            setMobileNoErrorMsg(false);
            sendOtp();
            setActiveStep((prevActiveStep) => prevActiveStep + 1);
        } else {
            setMobileNoErrorMsg(true);
        }
    }

    const sendOtp = (e) => {
        const data = { userId, mobileNo };
        profileService.receiveOtp(data).then(
            response => {
                setOtpErrorMsg(false);
            },
            error => {
                setOtpErrorMsg(true);//to show incorrect otp error msg  
            }
        );
    }

    const handleSubmit = (e) => {
        const data = { userId, mobileNo, otp };
        profileService.changeMobileNo(data).then(
            response => {
                //show success message
                setOtpErrorMsg(false);
                setSuccessfull(true);
            },
            error => {
                //show error message 
                setOtpErrorMsg(true);
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
                            <div className="card-body">
                                <div className="col-md-12 row mb-2">
                                    <div className="d-flex flex-wrap flex-column align-items-center justify-content-center">
                                        <PhoneAndroidIcon style={{ fontSize: 60 }} />
                                        <Typography component="h1" variant="h6" color="inherit" noWrap>
                                            Change mobile number
                                        </Typography>
                                    </div>
                                </div>

                                {successfull === true ? (
                                    <div className='text-center alert alert-success text-success animated bounceIn'>
                                        <h5><CheckCircle /> Confirmation!</h5>
                                        <strong> Your mobile number has been changed successfully.</strong>
                                    </div>
                                ) : (
                                    <form className={classes.root}>
                                        <span> To change mobile number you need to verify it belong to you through OTP.
                                            OTP will be sent to your mobile.</span>
                                        <div className='col-md-12'>
                                            Your current mobile number is: {userInfo.mobileNo}
                                        </div>
                                        <div className="d-flex flex-wrap flex-column align-items-center justify-content-center">
                                            {activeStep === 0 ?
                                                <div className='col-md-6'>
                                                    <TextField id="mobileNumber" label="New mobile number" required
                                                        error={mobileNoErrorMsg}
                                                        helperText={mobileNoErrorMsg === true ? "Mobile number is required" : ''}
                                                        onChange={handleMobileNoChange} />
                                                </div>
                                                :
                                                <div className='col-md-6'>
                                                    <strong>New mobile number: {mobileNo}</strong>
                                                    <TextField id="otp" label="OTP" required
                                                        error={otpErrorMsg}
                                                        helperText={otpErrorMsg === true ? errorMsg : ''}
                                                        value={otp}
                                                        onChange={handleOtpChange} />
                                                    <div className="col-md-12 row">
                                                        <div className="col">
                                                            Enter 4-digit OTP sent to your mobile. Didn't receive OTP?
                                                            <Button variant="outline" color="primary" onClick={sendOtp}>
                                                                Resend OTP
                                                            </Button>
                                                        </div>
                                                    </div>
                                                </div>}
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

export default EditMobileNo;