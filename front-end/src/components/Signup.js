import React, { useState, useRef } from "react";
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from "react-redux";
import { ChevronLeft, ChevronRight } from '@material-ui/icons';
import { makeStyles } from '@material-ui/core/styles';
import Stepper from '@material-ui/core/Stepper';
import Step from '@material-ui/core/Step';
import StepLabel from '@material-ui/core/StepLabel';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import CheckButton from "react-validation/build/button";
import SignupInstruction from './SignupInstruction';
import SignupPersonalDetail from './SignupPersonalDetail';
import SignupContactDetail from './SignupContactDetail';
import SignupVerification from './SignupVerification';
import SignupSuccess from './SignupSuccess';
import { register } from "../actions/auth";
import Form from "react-validation/build/form";
import Alert from '@material-ui/lab/Alert';
import IconButton from '@material-ui/core/IconButton';
import Collapse from '@material-ui/core/Collapse';
import CloseIcon from '@material-ui/icons/Close';
import moment from 'moment';

const useStyles = makeStyles((theme) => ({
    root: {
        width: '100%',
    },
    backButton: {
        marginRight: theme.spacing(1),
    },
    instructions: {
        marginTop: theme.spacing(1),
        marginBottom: theme.spacing(1),
    },
}));

export const Signup = () => {
    let navigate = useNavigate();
    const form = useRef();
    const checkBtn = useRef();
    const { message } = useSelector(state => state.message);
    const dispatch = useDispatch();

    const classes = useStyles();
    const steps = getSteps();
    const [activeStep, setActiveStep] = useState(0);
    const [successful, setSuccessful] = useState(false);
    const [loading, setLoading] = useState(false);
    const [showAlert, setShowAlert] = useState(false);
    const [responseMsg, setResponseMsg] = useState('');
    const [agreeTerms, setAgreeTerms] = useState(false);
    const [allDataEntered, setAllDataEntered] = useState(false);


    const {
        personalDetail, cid, dob, fullName, gender, fatherName, fatherCid, motherName, motherCid, permanentPlaceName,
        permanentGeog, permanentDzongkhag,
    } = SignupPersonalDetail();

    const { contactDetail, mobileNo, otp, email, verificationCode, password, confirmPassword } = SignupContactDetail();

    function getSteps() {
        return ['Instructions', 'Personal details', 'Contact details', 'Verification'];
    }

    function getStepContent(stepIndex) {
        switch (stepIndex) {
            case 0:
                return <SignupInstruction />;
            case 1:
                return personalDetail;
            case 2:
                return contactDetail;
            case 3:
                return <SignupVerification agreeTerms={agreeTerms} setAgreeTerms={setAgreeTerms} data={{
                    cid: cid,
                    dob: dob,
                    fullName: fullName,
                    gender: gender,
                    mobileNo: mobileNo,
                    email: email,
                    fatherName: fatherName,
                    fatherCid: fatherCid,
                    motherName: motherName,
                    motherCid: motherCid,
                    permanentPlaceName: permanentPlaceName,
                    permanentGeog: permanentGeog,
                    permanentDzongkhag: permanentDzongkhag,

                }} />;
            default:
                return 'Unknown stepIndex';
        }
    }

    const handleNext = () => {
        setActiveStep((prevActiveStep) => prevActiveStep + 1);
        setResponseMsg('');
        // if (activeStep === 1) {
        //     if (cid === undefined) {
        //         setShowAlert(true);
        //         setResponseMsg('Check your citizenship detail first.')
        //     } else {
        //         setShowAlert(false);
        //         setActiveStep((prevActiveStep) => prevActiveStep + 1);
        //     }
        // } else if (activeStep === 2) {
        //     if (mobileNo === undefined || email === undefined || password === undefined || confirmPassword === undefined) {
        //         setShowAlert(true);
        //         setResponseMsg('Please enter all the fields.')
        //     } else if (password !== confirmPassword) {
        //         setShowAlert(true);
        //         setResponseMsg("Password doesn't match.")
        //     } else {
        //         setShowAlert(false);
        //         setActiveStep((prevActiveStep) => prevActiveStep + 1);
        //     }
        // } else if (activeStep === 3) {
        //     alert(agreeTerms)
        //     if (!agreeTerms) {
        //         setShowAlert(true);
        //         setResponseMsg('Please agree before you submit.')
        //     } else {
        //         setShowAlert(false);
        //         setAllDataEntered(true);
        //     }
        // } else {
        //     setShowAlert(false);
        //     setActiveStep((prevActiveStep) => prevActiveStep + 1);
        // }
    };

    const handleSubmit = (e) => {
        setResponseMsg('');
        // if (checkBtn.current.context._errors.length === 0) {
        // if (!agreeTerms) {
        //     setShowAlert(true);
        //     setResponseMsg('Please agree before you submit.')
        // } else {
        e.preventDefault();
        setSuccessful(false);
        setLoading(true);
        setResponseMsg('');
        let genderChar = 'F';
        if (gender === 'Male') {
            genderChar = 'M';
        }

        let sex = gender;
        let birthDate = dob;

        const data = {
            cid, fullName,
            birthDate,
            sex,
            mobileNo, email,
            fatherName, motherName,
            password, permanentDzongkhag
            , permanentGeog,
            permanentPlaceName,
            otp, verificationCode, confirmPassword
        };

        dispatch(register(data))
            .then(() => {
                setSuccessful(true);
                setLoading(false);
                setActiveStep((prevActiveStep) => prevActiveStep + 1);
            })
            .catch(() => {
                setShowAlert(true);
                setSuccessful(false);
                setLoading(false);
            });
        // }
    };

    const handleBack = () => {
        setActiveStep((prevActiveStep) => prevActiveStep - 1);
    };

    return (
        <div className={classes.root}>
            <Stepper activeStep={activeStep} alternativeLabel>
                {steps.map((label) => (
                    <Step key={label}>
                        <StepLabel>{label}</StepLabel>
                    </Step>
                ))}
            </Stepper>
            <div>
                {activeStep === steps.length ? (
                    <div>
                        <SignupSuccess data={{ fullName: fullName, gender: gender }} />
                    </div>
                ) : (
                    <div>
                        <Form onSubmit={handleSubmit} ref={form}>
                            <Typography className={classes.instructions}>{getStepContent(activeStep)}</Typography>
                            <div className='col-md-12'>
                                <hr />
                                <div className='d-flex justify-content-end'>
                                    <Button variant="contained" color="primary"
                                        disabled={activeStep === 0}
                                        onClick={handleBack}
                                        className={classes.backButton}
                                    > <ChevronLeft /> Back </Button>
                                    {activeStep === steps.length - 1 ?
                                        <Button variant="contained" color="primary" onClick={handleSubmit}
                                            disabled={loading}>
                                            {loading && (
                                                <span className="spinner-border spinner-border-sm"></span>
                                            )} Submit
                                        </Button> :
                                        <Button variant="contained" color="primary" onClick={handleNext}>
                                            Next <ChevronRight />
                                        </Button>}
                                </div>
                                <div className="mb-2"></div>
                                {/* <CheckButton style={{ display: "none" }} ref={checkBtn} /> */}
                                <Collapse in={showAlert}>
                                    <Alert
                                        severity='error'
                                        action={
                                            <IconButton
                                                aria-label="close"
                                                color="inherit"
                                                size="small"
                                                onClick={() => {
                                                    setShowAlert(false);
                                                }}
                                            >
                                                <CloseIcon fontSize="inherit" />
                                            </IconButton>
                                        }
                                    >
                                        {message} {responseMsg}
                                    </Alert>
                                </Collapse>

                                {/* <Button color="outline-success btn-sm" className="fa fa-pencil pull-right" size="sm"
                                    onClick={() => { alert(agreeTerms) }}
                                >   Check here
                                </Button> */}
                            </div>
                        </Form>
                    </div>
                )}
            </div>
        </div>
    );
}

export default Signup;
