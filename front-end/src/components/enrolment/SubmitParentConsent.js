import React, { useState, useMemo, useEffect } from "react";
import { useSelector } from "react-redux";
import Button from '@material-ui/core/Button';
import { makeStyles } from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import FormHelperText from '@material-ui/core/FormHelperText';
import { useNavigate } from 'react-router-dom';
import Typography from '@material-ui/core/Typography';
import ChevronLeftRoundedIcon from '@material-ui/icons/ChevronLeftRounded';
import parentConsentService from "../../services/parentConsent.service";
import { ArrowBack, ArrowForward, CheckCircle } from '@material-ui/icons';
import LockOpenOutlinedIcon from '@material-ui/icons/LockOpenOutlined';
import { ChevronLeft, ChevronRight } from '@material-ui/icons';
import ChevronRightRoundedIcon from '@material-ui/icons/ChevronRightRounded';
import Alert from '@material-ui/lab/Alert';
import IconButton from '@material-ui/core/IconButton';
import Collapse from '@material-ui/core/Collapse';

import CloseIcon from '@material-ui/icons/Close';
import profileService from "../../services/profile.service";

const useStyles = makeStyles((theme) => ({
    root: {
        padding: theme.spacing(0, 5),
        '& .MuiTextField-root': {
            width: '100%',
            borderRadius: '25px',
        },
    },
}));


const SubmitParentConsent = () => {
    const classes = useStyles();
    const navigate = useNavigate();
    const { user: currentUser } = useSelector((state) => state.auth);
    const [userInfo, setUserInfo] = useState([]);
    const [fullName, setFullName] = useState('');
    const [mobileNo, setMobileNo] = useState('');
    const [guardianName, setGuardianName] = useState('');
    const [guardianEmail, setGuardianEmail] = useState('');
    const [relationToGuardian, setRelationToGuardian] = useState('');
    const [guardianMobileNo, setGuardianMobileNo] = useState('');
    const [otp, setOtp] = useState('');
    const [showOtpErrorMsg, setShowOtpErrorMsg] = useState(false);
    const [otpErrorMsg, setOtpErrorMsg] = useState('');
    const [showErrorMsg, setShowErrorMsg] = useState(false);
    const [errorMsg, setErrorMsg] = useState('');
    const [successfull, setSuccessfull] = useState(false);
    const [loading, setLoading] = useState(false);
    const [showLegalTerms, setLegalTerms] = useState(false);
    const [activeStep, setActiveStep] = useState(0);

    let userId = currentUser.userId;

    useEffect(() => {
        getProfileInfo();
    }, []);

    const getProfileInfo = () => {
        profileService.getProfileInfo(userId).then(
            response => {
                setUserInfo(response.data);
                setFullName(response.data.fullName);
                setMobileNo(response.data.mobileNo);
                setGuardianEmail(response.data.guardianEmail);
                setRelationToGuardian(response.data.relationToGuardian);
                setGuardianMobileNo(response.data.guardianMobileNo);
                setGuardianName(response.data.guardianName);
            },
            error => {
                console.log(
                    (error.response && error.response.data && error.response.data.message) ||
                    error.message || error.toString()
                );
            }
        );
    };


    const handleOtpChange = (e) => {
        setOtp(e.target.value);
        setOtpErrorMsg('');
        setShowOtpErrorMsg(false);
    };

    const toggleLegalTerms = () => {
        setLegalTerms(!showLegalTerms);
    };

    const handleNext = (e) => {
        receiveOtp();
    }

    const receiveOtp = () => {
        const data = { userId, guardianMobileNo, fullName, guardianName, guardianEmail, relationToGuardian };
        parentConsentService.receiveOtp(data).then(
            response => {
                setActiveStep((prevActiveStep) => prevActiveStep + 1);
            },
            error => {
                setLoading(false);
                setSuccessfull(false);
                setShowErrorMsg(true);
                setErrorMsg(error.response.data.message);
            }
        );
    }


    const handleSubmit = (e) => {
        if (otp === 4) {
            setShowOtpErrorMsg(true);
            setOtpErrorMsg('Enter 4 digit OTP');
        } else {
            setLoading(true);
            const data = { userId, fullName, guardianName, guardianMobileNo, mobileNo, guardianEmail, otp };
            parentConsentService.submitParentConsent(data).then(
                response => {
                    //show success message
                    setSuccessfull(true);
                    setLoading(false);
                    setShowErrorMsg(false);
                },
                error => {
                    //show error message 
                    setLoading(false);
                    setSuccessfull(false);
                    setShowErrorMsg(true);
                    setErrorMsg(error.response.data.message);
                }
            );
        }
    }

    return (
        <>
            <div className="col-md-12 row">
                <div className="d-flex flex-wrap flex-column align-items-center justify-content-center">
                    <div className="col-md-8 text-muted">
                        <div className="cards">
                            <div className="card-body">
                                <div className="col-md-12 row mb-2">
                                    <div className="d-flex flex-wrap flex-column align-items-center justify-content-center">
                                        {/* <LockOpenOutlinedIcon style={{ fontSize: 60 }} /> */}
                                        <Typography component="h1" variant="h6" color="inherit" noWrap>
                                            Parent consent
                                        </Typography>
                                    </div>
                                </div>
                                {successfull === true ? (
                                    <div className='text-center alert alert-success text-success animated bounceIn'>
                                        <h5><CheckCircle /> Confirmation!</h5>
                                        <strong> Your parent/guardian consent has been submitted successfully.</strong>
                                    </div>
                                ) : (
                                    <div className={classes.root}>
                                        <div className='col-md-12 row mb-2'>
                                            <span>
                                                You are required to submit parent/guardian consent because you
                                                are below 18 on enlistment day.
                                            </span>
                                            <span className="cursor-pointer infoMsg" onClick={toggleLegalTerms}>
                                                Please click here to read legal terms and conditions
                                            </span>
                                        </div>
                                        <div>
                                            {activeStep === 0 ? (<>
                                                <div className='mb-2'>
                                                    Guardian information
                                                </div>
                                                <div className='mb-2'>
                                                    Name: <strong>{userInfo.guardianName}</strong>
                                                </div>
                                                <div className='mb-2'>
                                                    Mobile Number: <strong>{userInfo.guardianMobileNo}</strong>
                                                </div>
                                                <div className='mb-2'>
                                                    Email: <strong>{userInfo.guardianEmail}</strong>
                                                </div>
                                                <div className='mb-2'>
                                                    Relation: <strong>{userInfo.relationToGuardian}</strong>
                                                </div>
                                                <div className='mb-2'>
                                                    OTP will be sent to {userInfo.guardianMobileNo}. Please ask your guardian to share you the
                                                    OTP.
                                                </div></>)
                                                : (<>
                                                    <div className='mb-2'>
                                                        Enter 4-digit OTP sent to mobile {guardianMobileNo} of {guardianName}.
                                                        Didn't receive OTP?
                                                        <Button variant="outline" color="primary"
                                                            onClick={receiveOtp}> Resend OTP</Button>
                                                    </div>
                                                    <div className='mb-2'>
                                                        <TextField
                                                            label="OTP"
                                                            required
                                                            error={showOtpErrorMsg}
                                                            helperText={showOtpErrorMsg === true ? otpErrorMsg : ''}
                                                            value={otp}
                                                            onChange={handleOtpChange}
                                                        />
                                                    </div></>
                                                )
                                            }
                                        </div>

                                        <div className="d-flex flex-wrap justify-content-end mb-4">
                                            {/* <small>
                                                Please enter 4 digit OTP sent to guardian mobile number.
                                            </small> */}
                                            {/* <div></div> */}

                                            {activeStep === 0 ?
                                                <Button variant="contained" color="primary" onClick={handleNext}>
                                                    Next <ChevronRightRoundedIcon />
                                                </Button> :
                                                <Button variant="contained" color="primary" onClick={handleSubmit}
                                                    disabled={loading}>
                                                    {loading && (
                                                        <span className="spinner-border spinner-border-sm"></span>
                                                    )} Submit
                                                </Button>}
                                        </div>

                                        <div className="animated bounceIn" hidden={!showErrorMsg}>
                                            <Collapse in={showErrorMsg}>
                                                <Alert
                                                    severity='error'
                                                    action={
                                                        <IconButton
                                                            aria-label="close"
                                                            color="inherit"
                                                            size="small"
                                                            onClick={() => {
                                                                setShowErrorMsg(false);
                                                            }}
                                                        >
                                                            <CloseIcon fontSize="inherit" />
                                                        </IconButton>
                                                    }
                                                >
                                                    {errorMsg}
                                                </Alert>
                                            </Collapse>
                                        </div>

                                        <div hidden={!showLegalTerms} className="mb-4 animated fadeIn">
                                            <strong>Legal terms and condition for parent</strong>
                                            <div className="mb-2">
                                                Your parents must be aware following points:
                                            </div>
                                            <div className="mb-2">
                                                <ul>
                                                    <li>
                                                        You agree that the information provided above is your true parent or legal guardian.
                                                        Providing woring information will result in taking legal action.
                                                    </li>
                                                    <li>
                                                        You agree that your parents/guardian is
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            </div >
        </>
    )
}

export default SubmitParentConsent;