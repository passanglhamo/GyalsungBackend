import React, { useState, useEffect } from "react";
import { useSelector } from "react-redux";
import { makeStyles } from '@material-ui/core/styles';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import { ArrowBack, ArrowForward, CheckCircle } from '@material-ui/icons';
import TextField from '@material-ui/core/TextField';
import profileService from "../../services/profile.service";
import enrolmentService from "../../services/enrolment.service";
import fieldSpecializationService from "../../services/fieldSpecialization.service";
import Chip from '@material-ui/core/Chip';

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

const EnrolmentCourseSelection = (props) => {
    const { user: currentUser } = useSelector((state) => state.auth);
    const classes = useStyles();
    const [coursePreference, setCoursePreference] = useState([]);
    const [allCourses, setAllCourses] = useState([]);
    const [allMathRequiredCourses, setAllMathRequiredCourses] = useState([]);
    const [allDefaultCourses, setAllDefaultCourses] = useState([]);
    const [userInfo, setUserInfo] = useState([]);
    const [mobileNo, setMobileNo] = useState(undefined);
    let userId = currentUser.userId;


    useEffect(() => {
        getCourseList();
        getAllMathRequiredCourses();
        getAllDefaultCourses();
    }, []);
    // useEffect(() => {
    //     getProfileInfo();
    // }, []); 

    const getCourseList = () => {
        fieldSpecializationService.findAllByStatus().then(
            response => {
                // setAllCourses(response.data._embedded.fieldSpecializations)
                setAllCourses(response.data)
            },
            error => {

            }
        );
    }

    const getAllMathRequiredCourses = () => {
        fieldSpecializationService.findAllMathRequiredCourses().then(
            response => {
                setAllMathRequiredCourses(response.data)
            },
            error => {

            }
        );
    }
    const getAllDefaultCourses = () => {
        fieldSpecializationService.findAllDefaultCourses().then(
            response => {
                setAllDefaultCourses(response.data)
            },
            error => {

            }
        );
    }

    // const getProfileInfo = () => {
    //     profileService.getProfileInfo(userId).then(
    //         response => {
    //             setUserInfo(response.data);
    //         },
    //         error => {
    //             console.log(
    //                 (error.response && error.response.data && error.response.data.message) ||
    //                 error.message || error.toString()
    //             );
    //         }
    //     );
    // };


    return {
        coursePreference, courseSelection: (
            <div className="col-md-12 row text-muted">
                <div className='mb-2'>
                    <strong>You will be required to undergo training under one of the following courses:</strong>
                </div>

                {allCourses && allCourses.map((items, idx) => {
                    return (
                        <div className='mb-1' key={idx}>
                            <CheckCircle />  {items.name}
                        </div>
                    );
                })}


                <div className='mb-2'>
                    <mark>
                        Note: For
                        {allMathRequiredCourses && allMathRequiredCourses.map((items, idx) => {
                            return (
                                <span key={idx}> {items.name},  </span>
                            );
                        })}
                        you need mathematics subject.
                        If you are illetrate or school dropouts by default you will be
                        placed to {allDefaultCourses && allDefaultCourses.map((items, idx) => {
                            return (
                                <span key={idx}> {items.name},  </span>
                            );
                        })}

                        Please not that it doesn't guarentee that you will be selected for your choice of preference.
                        Gyalsung HQ will allocate Training Academy based on your choice of preference and ranking.
                    </mark>
                </div>

                <div className='mb-2'>
                    Please write your preference against each courses below.
                </div>
                <div className='mb-2'>
                    <ul>
                        {allCourses && allCourses.map((items, idx) => {
                            return (
                                <li key={idx}> {items.name} </li>
                            );
                        })}
                    </ul>
                </div>

            </div>
        )
    }
}

export default EnrolmentCourseSelection;
