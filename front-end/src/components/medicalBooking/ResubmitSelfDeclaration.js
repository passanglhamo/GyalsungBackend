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

import ChevronLeftRoundedIcon from '@material-ui/icons/ChevronLeftRounded';
import InputLabel from '@material-ui/core/InputLabel';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import {
    faMedkit, faLocationDot, faHome, faPhone, faCheckCircle, faArrowLeft, faCloudDownload, faArrowRight, faChevronLeft, faChevronRight, faEdit, faIdBadge, faUser, faInfoCircle, faGraduationCap, faAward, faAdd,
    faLayerGroup, faHandsHolding, faHandsHelping, faFaceSmileBeam, faClock, faMale, faFemale, faBriefcase, faBook, faIdCard, faBirthdayCake, faLink, faLocation,
} from "@fortawesome/free-solid-svg-icons";

import Box from "@mui/material/Box";

import FormControl from '@material-ui/core/FormControl';
import { Switch } from "@mui/material";
import medicalbookingService from "../../services/medicalbooking.service";

const ResubmitSelfDeclaration = () => {
    const navigate = useNavigate();
    const [successful, setSuccessful] = useState(false);
    const [loading, setLoading] = useState(false);
    const [showAlert, setShowAlert] = useState(false);
    const [responseMsg, setResponseMsg] = useState('');

    const [allMedicalQuestion, setAllMedicalQuestion] = useState([]);

    const { user: currentUser } = useSelector((state) => state.auth);

    let userId = currentUser.userId;


    useEffect(() => {
        getAllMedicalQuestion();
    }, []);


    const getAllMedicalQuestion = () => {
        medicalbookingService.getAllMedicalQuestion().then(
            response => {
                setAllMedicalQuestion(response.data);
                console.log(response.data);
            },
            error => {

            }
        );
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

        console.log(medicalQuestionDtos)
        const data = { userId, medicalQuestionDtos };

        medicalbookingService.resubmitSelfDeclaration(data).then(
            response => {
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
        <>
            <div className="col-md-12 row mb-2">
                <div className="d-flex justify-content-between align-items-center">
                    <div>
                        <Button variant="contained" color="primary" size="small" onClick={() => navigate("/previousDeclaration")}>
                            <ChevronLeftRoundedIcon /> Go back </Button>
                    </div>
                    <strong>Resubmit self-declaration</strong>
                </div>
            </div>

            <div className="col-md-12 row">
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
                <div className="col-md-12 row">
                    <div className="mb-2">
                        This is replace your previous self-declaration. Please decide before you resubmit.
                    </div>
                    <div className="col-md-4">
                        <Button variant="contained" color="primary" onClick={handleSubmit}
                            disabled={loading}>
                            {loading && (
                                <span className="spinner-border spinner-border-sm"></span>
                            )}
                            Submit
                        </Button>
                    </div>
                </div>
            </div>
        </>
    )
}

export default ResubmitSelfDeclaration;