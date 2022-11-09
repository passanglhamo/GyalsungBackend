import React, { useState, useEffect, useRef } from "react";
import { useNavigate, Link } from 'react-router-dom';
import { useDispatch, useSelector } from "react-redux";
import Button from '@material-ui/core/Button';
import { ArrowBack, ArrowForward, CheckCircle } from '@material-ui/icons';
import ChevronLeftRoundedIcon from '@material-ui/icons/ChevronLeftRounded';
import { Switch } from "@mui/material";
import medicalbookingService from "../../services/medicalbooking.service";

const AppointmentList = () => {
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
        setLoading(true);
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

        medicalbookingService.resubmitSelfDeclaration(data).then(
            response => {
                setSuccessful(true);
                setLoading(false);
            },
            error => {
                setLoading(false);
                setSuccessful(false);
            }
        );
    };


    return (
        <>
            Appointmet List
        </>
    )
}

export default AppointmentList;