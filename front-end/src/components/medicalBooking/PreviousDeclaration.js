import React, { useState, useEffect } from "react";
import { useNavigate, Link } from 'react-router-dom';
import { useDispatch, useSelector } from "react-redux";
import Button from '@material-ui/core/Button';
import ChevronLeftRoundedIcon from '@material-ui/icons/ChevronLeftRounded';
import medicalbookingService from "../../services/medicalbooking.service";

const PreviousDeclaration = () => {
    const navigate = useNavigate();
    const [allMedicalQuestion, setAllMedicalQuestion] = useState([]);
    const { user: currentUser } = useSelector((state) => state.auth);
    let userId = currentUser.userId;

    useEffect(() => {
        getPreviousSelfDeclaration();
    }, []);

    const getPreviousSelfDeclaration = () => {
        medicalbookingService.getPreviousSelfDeclaration(userId).then(
            response => {
                setAllMedicalQuestion(response.data);
            },
            error => {
            }
        );
    }

    return (
        <>
            <div className="col-md-12 row mb-2">
                <div className="d-flex justify-content-between align-items-center">
                    <div>
                        <Button variant="contained" color="primary" size="small" onClick={() => navigate("/changeMedicalAppointment")}>
                            <ChevronLeftRoundedIcon /> Go back </Button>
                    </div>
                </div>
            </div>

            <div className="col-md-12 mb-2 d-flex justify-content-between align-items-center">
                <span>You have submitted following response</span>

                <span onClick={() => navigate("/resubmitSelfDeclaration")}>
                    Want to resubmit self-medical declaration? Click here
                </span>
            </div>
            <div className="col-md-12 row">
                {allMedicalQuestion && allMedicalQuestion.map((item, idx) => {
                    return (
                        <>
                            <div className='card mb-1'>
                                <div className='card-body p-2'>
                                    <div className='form-group row'>
                                        <span>{idx + 1}) {item.medicalQuestionName}</span>
                                        <span>Response: {item.checkStatus === 'Y' ? 'Yes' : 'No'}</span>
                                    </div>
                                </div>
                            </div>
                        </>
                    );
                })}
            </div>
        </>
    )
}

export default PreviousDeclaration;