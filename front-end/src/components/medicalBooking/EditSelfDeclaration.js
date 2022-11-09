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

const EditSelfDeclaration = () => {
    const [successful, setSuccessful] = useState(false);
    const [loading, setLoading] = useState(false);
    const [showAlert, setShowAlert] = useState(false);
    const [responseMsg, setResponseMsg] = useState('');

    const [previousDeclation, setPreviousDeclation] = useState([]);

    const [allMedicalQuestion, setAllMedicalQuestion] = useState([]);

    const { user: currentUser } = useSelector((state) => state.auth);

    let userId = currentUser.userId;


    useEffect(() => {
        getPreviousSelfDeclaration();
    }, []);

    const getPreviousSelfDeclaration = () => {
        medicalbookingService.getPreviousSelfDeclaration(userId).then(
            response => {

            },
            error => {

            }
        );
    }

    const handleSubmit = (e) => {
        const medicalQuestionDtos = [];
        allMedicalQuestion.map((val, idx) => {
            let question = {
                medicalQuestionId: val.id,
                medicalQuestionName: val.medicalQuestionName,
                checkStatus: val.isEnable === true ? 'Y' : 'N',
            };
            medicalQuestionDtos.push(question);
        });

        const data = { userId, medicalQuestionDtos };

        medicalbookingService.editMedicalAppointment(data).then(
            response => {
                // setActiveStep((prevActiveStep) => prevActiveStep + 1);
            },
            error => {
                console.log(
                    (error.response && error.response.data && error.response.data.message) ||
                    error.message || error.toString()
                );
            }
        );
    };


    return (
        <div className="col-md-12 row">

            EditSelfDeclarationss
        </div>
    )
}

export default EditSelfDeclaration;