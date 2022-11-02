import React, { useState, useRef, useEffect } from "react";
import { useNavigate, Link } from 'react-router-dom';
import { useDispatch, useSelector } from "react-redux";
import { ChevronLeft, ChevronRight } from '@material-ui/icons';
import { makeStyles } from '@material-ui/core/styles';
import Stepper from '@material-ui/core/Stepper';
import Step from '@material-ui/core/Step';
import StepLabel from '@material-ui/core/StepLabel';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import EnrolmentPersonalDetail from "./EnrolmentPersonalDetail";
import Form from "react-validation/build/form";
import Alert from '@material-ui/lab/Alert';
import IconButton from '@material-ui/core/IconButton';
import Collapse from '@material-ui/core/Collapse';
import CloseIcon from '@material-ui/icons/Close';
import { ArrowBack, ArrowForward, CheckCircle } from '@material-ui/icons';
import ReactCountryFlag from "react-country-flag";
import moment from 'moment';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
    faEnvelope, faLocationDot, faHome, faPhone, faCheckCircle, faArrowLeft, faCloudDownload, faArrowRight, faChevronLeft, faChevronRight, faEdit, faIdBadge, faUser, faInfoCircle, faGraduationCap, faAward, faAdd,
    faLayerGroup, faHandsHolding, faHandsHelping, faFaceSmileBeam, faClock, faMale, faFemale, faBriefcase, faBook, faIdCard, faBirthdayCake, faLink, faLocation,
} from "@fortawesome/free-solid-svg-icons";
import profileService from "../../services/profile.service";
import EnrolmentInstruction from "./EnrolmentInstruction";
import EnrolmentFamilyDetail from "./EnrolmentFamilyDetail";
import EnrolmentCourseSelection from "./EnrolmentCourseSelection";
import EnrolmentVerification from "./EnrolmentVerification";
import enrolmentService from "../../services/enrolment.service";


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
    const { user: currentUser } = useSelector((state) => state.auth);
    const dispatch = useDispatch();
    const classes = useStyles();
    const steps = getSteps();
    const [activeStep, setActiveStep] = useState(0);
    const [successful, setSuccessful] = useState(false);
    const [loading, setLoading] = useState(false);
    const [showAlert, setShowAlert] = useState(false);
    const [responseMsg, setResponseMsg] = useState('');
    const [userInfo, setUserInfo] = useState([]);
    const [presentDzongkhagName, setPresentDzongkhagName] = useState('');
    const [presentGeogName, setPresentGeogName] = useState('');
    const [presentCountry, setPresentCountry] = useState('');
    const [enrolmentFileDtos] = useState([]);
    let userId = currentUser.userId;

    useEffect(() => {
        getProfileInfo();
    }, []);

    const getProfileInfo = () => {
        profileService.getProfileInfo(userId).then(
            response => {
                setUserInfo(response.data);
                setPresentDzongkhagName(response.data.presentDzongkhag.dzongkhagName);
                setPresentGeogName(response.data.presentGeog.geogName);
                setPresentCountry(response.data.presentCountry);
            },
            error => {
                console.log(
                    (error.response && error.response.data && error.response.data.message) ||
                    error.message || error.toString()
                );
            }
        );
    };

    let presentAddress = userInfo.presentPlaceName + ", " + presentGeogName + ", " + presentDzongkhagName + " " + presentCountry;
    let permanantAddress = userInfo.permanentPlaceName + ", " + userInfo.permanentGeog + ", " + userInfo.permanentDzongkhag;
    if (presentCountry !== 'Bhutan') {
        presentAddress = userInfo.presentPlaceName + ", " + presentCountry;
    }

    const { personalDetail } = EnrolmentPersonalDetail(userInfo, presentGeogName, presentDzongkhagName);
    const { familyDetail, uploadedFiles } = EnrolmentFamilyDetail(userInfo);
    const { courseSelection, coursePreference } = EnrolmentCourseSelection(userInfo);
    const { verification, torAgreed } = EnrolmentVerification(userInfo, uploadedFiles);

    function getSteps() {
        return ['Instructions', 'Personal details', 'Family details', 'Field specialization', 'Verification'];
    }

    function getStepContent(stepIndex) {
        switch (stepIndex) {
            case 0:
                return <EnrolmentInstruction />;
            case 1:
                return personalDetail;
            case 2:
                return familyDetail;
            case 3:
                return courseSelection;
            case 4:
                return verification;
            default:
                return 'Unknown stepIndex';
        }
    }

    const handleNext = () => {
        setActiveStep((prevActiveStep) => prevActiveStep + 1);
    };

    const handleBack = () => {
        setActiveStep((prevActiveStep) => prevActiveStep - 1);
    };

    const handleSubmit = (e) => {
        let data = new FormData();
        data.append("userId", userId);
        for (var index = 0; index < uploadedFiles.length; index++) {
            data.append("enrolmentInfoFiles", uploadedFiles[index]);
        }
        enrolmentService.save(data).then(
            response => {
                setActiveStep((prevActiveStep) => prevActiveStep + 1);
            },
            error => {
                console.log(
                    (error.response && error.response.data && error.response.data.message) ||
                    error.message || error.toString()
                );
            }
        );
    };


    function getCurrentDate() {
        return moment(new Date()).format('MMM DD, YYYY');
    };

    function getSalutation() {
        let salutation = 'Mr.'
        let gender = userInfo.sex;
        if (gender === 'FEMALE') {
            salutation = 'Ms.'
        }
        return salutation;
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
                        <div className='alert alert-successee'>
                            <div className='text-center alert alert-success text-success'>
                                <div className='mb-2'>
                                    <h4> Confirmation</h4>
                                </div>
                                <div className='dropdown-divider'></div>
                                <div className='mb-2'>
                                    <h1><FontAwesomeIcon icon={faCheckCircle} /></h1>
                                    <strong>You have successfully enrolled to Gyalsung Enlishtment</strong>
                                </div>
                            </div>
                            <div className=''>
                                <p>{getSalutation()} {userInfo.fullName},</p>
                                <p>Thank you for enrolling to Gyalsung Enlistment on {getCurrentDate()}. You are required to book medical examination
                                    screening. You may book nearest medical examination center depending availabiliy.
                                </p>

                            </div>
                            <div className='dropdown-divider'></div>
                            <div className='row mb-3'>
                                <div className='text-center'>
                                    <Button size="small" variant="contained" color="primary" onClick={() => navigate("/medicalBooking")}>
                                        Okay,  proceed to medical screening booking <ArrowForward />
                                    </Button>
                                </div>
                            </div>
                        </div>
                    </div>
                ) : (
                    <div>
                        {/* <Form onSubmit={handleSubmit} ref={form}> */}
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
                        {/* </Form> */}
                    </div>
                )}
            </div>
        </div>
    );
}

export default Signup;
