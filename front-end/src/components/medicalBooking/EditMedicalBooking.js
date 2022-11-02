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
import ReactCountryFlag from "react-country-flag";
import { Card, CardBody, Col, Label, FormGroup, } from 'reactstrap';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

import FormControl from '@material-ui/core/FormControl';
import InputLabel from '@material-ui/core/InputLabel';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import {
    faMedkit, faLocationDot, faHome, faPhone, faCheckCircle, faArrowLeft, faCloudDownload, faArrowRight, faChevronLeft, faChevronRight, faEdit, faIdBadge, faUser, faInfoCircle, faGraduationCap, faAward, faAdd,
    faLayerGroup, faHandsHolding, faHandsHelping, faFaceSmileBeam, faClock, faMale, faFemale, faBriefcase, faBook, faIdCard, faBirthdayCake, faLink, faLocation,
} from "@fortawesome/free-solid-svg-icons";

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


const EditMedicalBooking = () => {
    const classes = useStyles();
    const [allDzongkhag, setAllDzongkhag] = useState([]);
    const [allHospitals, setAllHospitals] = useState([]);
    const [selectedDzongkhagId, setSelectedDzongkhagId] = useState('');
    const [selectedHospitalId, setSelectedHospitalId] = useState('');



    const handleDzongkhagChange = (e) => {
        setSelectedDzongkhagId(e.target.value);
        // getAllActiveHospitalsByDzongkhagId(e.target.value);
    }
    const handleHospitalChange = (e) => {
        setSelectedHospitalId(e.target.value);
        // getAllAvailableTimeSlotByHospitalId();
    }
    return (
        <div className="col-md-12 row">
            <div className="d-flex flex-wrap flex-column align-items-center justify-content-center">
                <div className="col-md-10 text-muted">
                    <p>
                        You have booked Tashigang Hospital on Oct 12, 2022 at 02:00 PM - 3:00 PM <br></br>
                    </p>
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
            </div>
        </div>
    )
}

export default EditMedicalBooking