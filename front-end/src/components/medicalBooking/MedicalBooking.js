import React, { useState, useEffect, useRef } from "react";
import { useNavigate, Link } from 'react-router-dom';
import { useDispatch, useSelector } from "react-redux";
import { ChevronLeft, ChevronRight } from '@material-ui/icons';
import { makeStyles } from '@material-ui/core/styles';
import Stepper from '@material-ui/core/Stepper';
import Step from '@material-ui/core/Step';
import StepLabel from '@material-ui/core/StepLabel';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import Form from "react-validation/build/form";
import Alert from '@material-ui/lab/Alert';
import IconButton from '@material-ui/core/IconButton';
import Collapse from '@material-ui/core/Collapse';
import CloseIcon from '@material-ui/icons/Close';
import CloudDownloadIcon from '@material-ui/icons/CloudDownload';
import { ArrowBack, ArrowForward, CheckCircle } from '@material-ui/icons';
import moment from 'moment';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Chip from '@mui/material/Chip';

import FormControl from '@material-ui/core/FormControl';
import InputLabel from '@material-ui/core/InputLabel';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import {
    faMedkit, faLocationDot, faHome, faPhone, faCheckCircle, faArrowLeft, faCloudDownload, faArrowRight, faChevronLeft, faChevronRight, faEdit, faIdBadge, faUser, faInfoCircle, faGraduationCap, faAward, faAdd,
    faLayerGroup, faHandsHolding, faHandsHelping, faFaceSmileBeam, faClock, faMale, faFemale, faBriefcase, faBook, faIdCard, faBirthdayCake, faLink, faLocation,
} from "@fortawesome/free-solid-svg-icons";

import medicalbookingService from "../../services/medicalbooking.service";
import Box from "@mui/material/Box";
import MedicalServicesIcon from '@mui/icons-material/MedicalServices';
import { Switch } from "@mui/material";

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

export const MedicalBooking = () => {
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
    const [allDzongkhag, setAllDzongkhag] = useState([]);
    const [allHospitals, setAllHospitals] = useState([]);
    const [selectedDzongkhagId, setSelectedDzongkhagId] = useState('');
    const [selectedHospitalId, setSelectedHospitalId] = useState('');

    const [availableDateTimeSlots, setAvailableDateTimeSlots] = useState([]);
    const [tempAvailableDateTimeSlots, setTempAvailableDateTimeSlots] = useState([]);
    const [availableDates, setAvailableDates] = useState([]);
    const [availableTimeSlots, setAvailableTimeSlots] = useState([]);

    const [selectedDate, setSelectedDate] = useState(undefined);
    const [selectedTime, setSelectedTime] = useState(undefined);
    const [scheduleTimeId, setScheduleTimeId] = useState('');
    const [allMedicalQuestion, setAllMedicalQuestion] = useState([]);
    const { user: currentUser } = useSelector((state) => state.auth);

    let userId = currentUser.userId;
    const dateFormat = "MMMM DD, YYYY";
    const timeFormat = "hh:mm A";
    let defaultDate = null;
    const questionAndAnswer = {};
    const [answers, setAnswers] = useState({});

    useEffect(() => {
        getAllMedicalQuestion();
        getAllDzongkhag();
    }, []);

    useEffect(() => {
        getAllActiveHospitalsByDzongkhagId();
    }, [selectedDzongkhagId]);

    useEffect(() => {
        getAllAvailableAppointmentDateByHospitalId();
    }, [selectedHospitalId]);

    useEffect(() => {
        getAvailableTimeSlots();
    }, [selectedHospitalId]);

    const getAllMedicalQuestion = () => {
        medicalbookingService.getAllMedicalQuestion().then(
            response => {
                setAllMedicalQuestion(response.data);
            },
            error => {

            }
        );
    }
    const getAllDzongkhag = () => {
        medicalbookingService.getAllDzongkhag().then(
            response => {
                setAllDzongkhag(response.data);
            },
            error => {

            }
        );
    }

    const getAllActiveHospitalsByDzongkhagId = (selectedDzongkhagId) => {
        medicalbookingService.getAllActiveHospitalsByDzongkhagId(selectedDzongkhagId).then(
            response => {
                setAllHospitals(response.data);
            },
            error => {

            }
        );
    }

    const getAllAvailableAppointmentDateByHospitalId = (selectedHospitalId) => {
        medicalbookingService.getAllAvailableAppointmentDateByHospitalId(selectedHospitalId).then(
            async response => {
                setAvailableDateTimeSlots(response.data);
                let availableDates = [];
                response.data.filter(element => {
                    const isDuplicate = availableDates.includes(moment(element.appointmentDate).format(dateFormat));
                    if (!isDuplicate) {
                        availableDates.push(moment(element.appointmentDate).format(dateFormat));
                        return true;
                    }
                    return false;
                });
                setAvailableDates(availableDates);
                defaultDate = availableDates[0];
                // getAvailableTimeSlots(availableDates[0]);
            },
            error => { }
        );

    }

    /*================Method to get list of available appointment times for the date=============*/
    const getAvailableTimeSlots = (date) => {
        setSelectedDate(`${date}`);
        setTempAvailableDateTimeSlots(availableDateTimeSlots.filter(item =>
            moment(item.appointmentDate).format(dateFormat) === date)
        );
        let availableTimeSlots = [];
        tempAvailableDateTimeSlots.map(timeSlot => {
            timeSlot.hospitalScheduleTimeListDtos.map(item => {
                availableTimeSlots.push(item);
                return true;
            });
            setAvailableTimeSlots(availableTimeSlots);
            return true;
        })

    }


    const toggleSwitch = (value, index) => {
        if (allMedicalQuestion[index].isEnable === value) return;
        const question = allMedicalQuestion.map((item, idx) => {
            let { isEnable } = item;
            if (index === idx) {
                isEnable = value;
            }
            return { ...item, isEnable };
        });
        setAllMedicalQuestion(question);
    };

    const handleDzongkhagChange = (e) => {
        setSelectedDzongkhagId(e.target.value);
        getAllActiveHospitalsByDzongkhagId(e.target.value);
    }

    const handleHospitalChange = (e) => {
        setSelectedHospitalId(e.target.value);
        getAllAvailableAppointmentDateByHospitalId(e.target.value);
    }

    function getSteps() {
        return ['Self declaration', 'Appointment booking'];
    }

    function getStepContent(stepIndex) {
        switch (stepIndex) {
            case 0:
                return <div className='text-muted'>
                    <p>
                        The applicant should complete a Medical Screening Questionnaire online. It is important to  declare pre-existing
                        medical conditions honestly to prepare for the training and for Gyalsung HQ to provide timely interventions and
                        support (if required) for uninterrupted Training.
                    </p>
                    <p>
                        You are required to declare medical conditions yourself first and then proceed to booking. We collect self declaration
                        to study the mental state of individual. We maintain that this data is highly confidential.
                    </p>
                    <div className='mb-2'>
                        <mark>
                            Please check YES or NO against each questionnaire below:
                        </mark>
                    </div>

                    {allMedicalQuestion && allMedicalQuestion.map((item, idx) => {
                        return (
                            <>
                                <div className='card mb-1'>
                                    <div className='card-body p-2'>
                                        <div className='form-group row'>
                                            <div className='col-md-10'>
                                                <strong>{item.medicalQuestionName}</strong>
                                            </div>
                                            {/* Slider*/}
                                            <div className='col-md-2'>
                                                <strong> No </strong>
                                                <Switch onChange={() => { toggleSwitch(!item.isEnable, idx); }}
                                                    value={item.isEnabled}
                                                    keyV={idx}
                                                />
                                                <strong> Yes </strong>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </>
                        );
                    })}
                </div>;
            case 1:
                return <div className="col-md-12 row">
                    <div className="d-flex flex-wrap flex-column align-items-center justify-content-center">
                        <div className="col-md-10 text-muted">
                            <p>
                                You are required book a medical screening appointment at the nearest identified hospital in your
                                respective dzongkhag. <br></br>
                            </p>
                            {/*Dzongkhag selection drop down*/}
                            <div className="mb-2">
                                <FormControl className={classes.root}>
                                    <InputLabel id="select-label required">Dzongkhag *</InputLabel>
                                    <Select labelId="select-ul-label" value={selectedDzongkhagId}
                                        onChange={handleDzongkhagChange}>
                                        {allDzongkhag && allDzongkhag.map((items, idx) => {
                                            return (
                                                <MenuItem key={idx} value={items.dzongkhagId}><span>{items.dzongkhagName}</span></MenuItem>
                                            );
                                        })}
                                    </Select>
                                </FormControl>
                            </div>
                            {/*Hospital selection drop down*/}
                            <div className="mb-2">
                                <FormControl className={classes.root}>
                                    <InputLabel id="select-label required">Hospital *</InputLabel>
                                    <Select labelId="select-ul-label" value={selectedHospitalId}
                                        onChange={handleHospitalChange}>
                                        {allHospitals && allHospitals.map((items, idx) => {
                                            return (
                                                <MenuItem key={idx} value={items.hospitalId}><span>{items.hospitalName}</span></MenuItem>
                                            );
                                        })}
                                    </Select>
                                </FormControl>
                            </div>
                            <hr />
                            {/*Available dates for appointment*/}
                            {/*<div className="mb-2">*/}
                            {/*    {availableDates.map((date) => {*/}
                            {/*        return (*/}
                            {/*            <span className="animated fadeIn"><Chip clickable*/}
                            {/*                key={date}*/}
                            {/*                color="primary" variant="outlined"*/}
                            {/*                label={date}*/}
                            {/*                value={date}*/}
                            {/*                onClick={(e) => getAvailableTimeSlots(date)}*/}
                            {/*            /> </span>*/}
                            {/*        );*/}
                            {/*    })}*/}
                            {/*</div>*/}
                            {/*new appointment dates*/}
                            <Box
                                // border={1}
                                sx={{
                                    height: "20%",
                                    overflow: "scroll",
                                    display: "flex",
                                    alignItems: "center",
                                    gap: 1,
                                }}
                            >
                                {availableDates.map((date, index) => {
                                    return (
                                        <span className="animated fadeIn">
                                            <Chip
                                                clickable
                                                key={`${date}`}
                                                color="primary"
                                                variant={selectedDate === `${date}` ? undefined : "outlined"}
                                                label={date}
                                                value={date}
                                                onClick={(e) => getAvailableTimeSlots(date)}
                                            /> </span>
                                    );
                                })}
                            </Box>
                            {/*<br/>*/}
                            <hr />
                            {/*Available time for  appointment date*/}
                            {/*<div className="mb-2">*/}
                            {/*    {availableTimeSlots.map((data, index) => {*/}
                            {/*        const label = moment(data.startTime).format(timeFormat) + " - " +*/}
                            {/*            moment(data.endTime).format(timeFormat);*/}
                            {/*        return (*/}
                            {/*            <span className="animated fadeIn">*/}
                            {/*                <Chip clickable key={label} label={label} value={label} color="secondary" variant="outlined"*/}
                            {/*                    deleteIcon="Book it" />*/}
                            {/*                {'  '}*/}
                            {/*            </span>*/}
                            {/*        );*/}
                            {/*    })}*/}
                            {/*</div>*/}
                            <Box
                                // border={1}
                                sx={{
                                    height: "20%",
                                    overflow: "scroll",
                                    display: "flex",
                                    alignItems: "center",
                                    gap: 1,
                                }}
                            >
                                {availableTimeSlots.map((data, index) => {
                                    const label = moment(data.startTime).format(timeFormat) + " - " +
                                        moment(data.endTime).format(timeFormat);
                                    return (
                                        <span className="animated fadeIn">
                                            <Chip
                                                clickable
                                                key={label}
                                                label={label}
                                                value={label}
                                                color="secondary"
                                                variant={selectedTime === `${label}` ? undefined : "outlined"}
                                                deleteIcon="Book it"
                                                onClick={() => { setSelectedTime(`${label}`); setScheduleTimeId(data.id) }}
                                            />
                                            {'  '}
                                        </span>
                                    );
                                })}
                            </Box>
                            <hr />
                        </div>
                    </div>
                </div>
            default:
                return 'Unknown stepIndex';
        }
    }

    const handleNext = () => {
        setAnswers(questionAndAnswer);
        setActiveStep((prevActiveStep) => prevActiveStep + 1);
    };

    const handleSubmit = (e) => {
        let dzongkhagId = selectedDzongkhagId;
        let hospitalId = selectedHospitalId;

        const medicalQuestionDtos = [];
        allMedicalQuestion.map((val, idx) => {
            let question = {
                medicalQuestionId: val.id,
                medicalQuestionName: val.medicalQuestionName,
                checkStatus: val.isEnable === true ? 'Y' : 'N',
            };
            medicalQuestionDtos.push(question);
        });

        const data = { userId, dzongkhagId, hospitalId, scheduleTimeId, medicalQuestionDtos };

        medicalbookingService.bookMedicalAppointment(data).then(
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
                    <div className='alert alert-successee'>
                        <div className='text-center alert alert-success text-success'>
                            <div className='mb-2'>
                                <h4><FontAwesomeIcon icon={faMedkit} /> Confirmation</h4>
                            </div>
                            <div className='dropdown-divider'></div>
                            <div className='mb-2'>
                                <h1><FontAwesomeIcon icon={faCheckCircle} /></h1>
                                <strong>You have successfully booked medical screening appointment on July 27, 2022 at Gidakom Hospital, Thimphu.</strong><br></br>
                                <small>You may choose to edit before 10 days from now.</small>
                            </div>
                        </div>
                        <div className=''>
                            <p>Mr. Ngawang Zepa,</p>
                            <p>Thank you for booking medical screening appointment on July 27, 2022 at Gidakom Hospital, Thimphu.
                                Please report to Gidakom Hospital, Thimphu at 09:00 AM on July 27, 2022.
                            </p>
                            <mark>You may choose to edit before 10 days from the appointment date you select after booking.</mark>
                            <p>
                                Thank you and see tou there on July 27, 2022.
                            </p>

                        </div>
                        <div className='dropdown-divider'></div>
                        <div className='row mb-3'>
                            <div className='text-center'>
                                <Button variant="contained" color="primary" onClick={() => navigate("/")}>
                                    <ArrowBack />  Back to home page
                                </Button>
                            </div>
                        </div>
                    </div>
                ) : (
                    <Box maxWidth="95%" alignItems="center" justifyContent="center">
                        <Form onSubmit={handleSubmit} ref={form}>
                            <Typography className={classes.instructions}>{getStepContent(activeStep)}</Typography>

                            <div className='col-md-12'>
                                {/*<hr />*/}
                                <div className='d-flex justify-content-end m-5 px-5'>
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
                            </div>
                        </Form>
                    </Box>
                )}
            </div>
        </div>
    );
}

export default MedicalBooking;
