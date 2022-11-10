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
import InputLabel from '@material-ui/core/InputLabel';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import {
    faMedkit, faLocationDot, faHome, faPhone, faCheckCircle, faArrowLeft, faCloudDownload, faArrowRight, faChevronLeft, faChevronRight, faEdit, faIdBadge, faUser, faInfoCircle, faGraduationCap, faAward, faAdd,
    faLayerGroup, faHandsHolding, faHandsHelping, faFaceSmileBeam, faClock, faMale, faFemale, faBriefcase, faBook, faIdCard, faBirthdayCake, faLink, faLocation,
} from "@fortawesome/free-solid-svg-icons";

import Box from "@mui/material/Box";

import FormControl from '@material-ui/core/FormControl';

import medicalbookingService from "../../services/medicalbooking.service";


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


const ChangeMedicalAppointment = () => {
    const classes = useStyles();
    const navigate = useNavigate();
    const [bookedHospitalName, setBookedHospitalName] = useState('');
    const [bookedDzongkhagName, setBookedDzongkhagName] = useState('');
    const [bookedDate, setBookedDate] = useState('');
    const [bookedStartTime, setBookedStartTime] = useState('');
    const [bookedEndTime, setBookedEndTime] = useState('');


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

    const { user: currentUser } = useSelector((state) => state.auth);

    let userId = currentUser.userId;

    const dateFormat = "MMMM DD, YYYY";
    const timeFormat = "hh:mm A";
    let defaultDate = null;

    useEffect(() => {
        getMedicalAppointmentDetail();
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

    const getMedicalAppointmentDetail = () => {
        medicalbookingService.getMedicalAppointmentDetail(userId).then(
            response => {
                setBookedHospitalName(response.data.hospitalName);
                setBookedDzongkhagName(response.data.dzongkhagName);
                setBookedDate(response.data.appointmentDate);
                setBookedStartTime(response.data.startTime);
                setBookedEndTime(response.data.endTime);
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
            },
            error => { }
        );
    }

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

    const handleDzongkhagChange = (e) => {
        setSelectedDzongkhagId(e.target.value);
        getAllActiveHospitalsByDzongkhagId(e.target.value);
    }

    const handleHospitalChange = (e) => {
        setSelectedHospitalId(e.target.value);
        getAllAvailableAppointmentDateByHospitalId(e.target.value);
    }

    const handleSubmit = (e) => {
        setLoading(true);
        let dzongkhagId = selectedDzongkhagId;
        let hospitalId = selectedHospitalId;

        const data = { userId, dzongkhagId, hospitalId, scheduleTimeId };

        medicalbookingService.changeMedicalAppointment(data).then(
            response => {
                setLoading(false);
                setSuccessful(true);
                getMedicalAppointmentDetail();
            },
            error => {
                setLoading(false);
                console.log(
                    (error.response && error.response.data && error.response.data.message) ||
                    error.message || error.toString()
                );
            }
        );
    };

    return (
        <>
            <div className="col-md-12 row">
                <div className="d-flex flex-wrap flex-column align-items-center justify-content-center">
                    <div className="col-md-10 text-muted">
                        <div>
                            <span>
                                You have booked {bookedHospitalName}, {bookedDzongkhagName} on {moment(bookedDate).format('MMM MM, YYYY')}
                                {' '} from  {moment(bookedStartTime).format(timeFormat)} - {moment(bookedEndTime).format(timeFormat)} <br></br>
                            </span>

                            <span onClick={() => navigate("/previousDeclaration")}>
                                Click here to check your self-declaration
                            </span>
                        </div>
                        {successful === true ? (<>
                            <div className='text-center alert alert-success text-success animated bounceIn'>
                                <h5><CheckCircle /> Confirmation!</h5>
                                <strong> You have successfully changed your medical appointment.</strong>
                            </div>
                        </>
                        ) : (<>
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
                            <Box
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
                            <hr />
                            <Box
                                sx={{
                                    height: "20%",
                                    overflow: "scroll",
                                    display: "flex",
                                    alignItems: "center",
                                    gap: 1,
                                }}
                            >
                                {availableTimeSlots.map((data, index) => {
                                    const label = moment(data.startDateTime).format(timeFormat) + " - " +
                                        moment(data.endDateTime).format(timeFormat);
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

                            <Button variant="contained" color="primary" onClick={handleSubmit}
                                disabled={loading}>
                                {loading && (
                                    <span className="spinner-border spinner-border-sm"></span>
                                )}
                                Submit
                            </Button>
                            <hr />
                        </>
                        )}
                    </div>
                </div>
            </div>
        </>
    )
}

export default ChangeMedicalAppointment;