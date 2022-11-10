import React, { useState, useEffect, useRef } from "react";
import { useNavigate, Link } from 'react-router-dom';
import { useDispatch, useSelector } from "react-redux";
import Button from '@material-ui/core/Button';
import { ArrowBack, ArrowForward, CheckCircle } from '@material-ui/icons';
import ChevronLeftRoundedIcon from '@material-ui/icons/ChevronLeftRounded';
import { Switch } from "@mui/material";
import medicalbookingService from "../../services/medicalbooking.service";
import { FormControl, InputLabel, Box, List, ListItemButton, Select, MenuItem, Stack, Typography, } from "@mui/material";
import { CheckBox } from "@mui/icons-material";
import TextField from "@mui/material/TextField";
import moment from 'moment';
import { Dialog, DialogActions, DialogContent, DialogTitle } from '@material-ui/core';
import Slide from '@material-ui/core/Slide';

const Transition = React.forwardRef(function Transition(props, ref) {
    return <Slide direction="up" ref={ref} {...props} />;
});

const AppointmentList = () => {

    const [allDzongkhag, setAllDzongkhag] = useState([]);
    const [allHospitals, setAllHospitals] = useState([]);
    const [selectedDzongkhagId, setSelectedDzongkhagId] = useState('');
    const [selectedHospitalId, setSelectedHospitalId] = useState('');
    const [allYear, setAllYear] = useState([{ "year": 2022 }, { "year": 2023 }]);
    const [selectedYear, setSelectedYear] = useState('');

    const [availableDates, setAvailableDates] = useState([]);
    const [availableTimeSlots, setAvailableTimeSlots] = useState([]);
    const [open, setOpen] = useState(false);
    const [bookedByFullName, setBookedByFullName] = useState('');
    const [allMedicalQuestion, setAllMedicalQuestion] = useState([]);

    const { user: currentUser } = useSelector((state) => state.auth);

    let userId = currentUser.userId;
    const dateFormat = "MMMM DD, YYYY";
    const timeFormat = "hh:mm A";
    let defaultDate = null;

    useEffect(() => {
        getAllDzongkhag();
    }, []);

    useEffect(() => {
        getAllActiveHospitalsByDzongkhagId();
    }, [selectedDzongkhagId]);


    const handleDzongkhagChange = (e) => {
        setSelectedDzongkhagId(e.target.value);
        getAllActiveHospitalsByDzongkhagId(e.target.value);
    }

    const handleHospitalChange = (e) => {
        setSelectedHospitalId(e.target.value);
    }

    const handleYearChangeChange = (e) => {
        setSelectedYear(e.target.value);
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

    const handleSearch = () => {
        getAllBookingDateByHospitalIdAndYear();
    };

    const getAllBookingDateByHospitalIdAndYear = () => {
        medicalbookingService.getAllBookingDateByHospitalIdAndYear(selectedHospitalId, selectedYear).then(
            async response => {
                setAvailableDates(response.data);
                // getAvailableTimeSlots(availableDates[0]); 
            },
            error => { }
        );
    }

    const getAvailableTimeSlots = (hospitalScheduleDateId) => {
        medicalbookingService.getTimeSlotsByScheduleDateId(hospitalScheduleDateId).then(
            async response => {
                setAvailableTimeSlots(response.data);
            },
            error => { }
        );
    }

    const getBookingDetail = (hos_schedule_time_id, booked_by, fullName) => {
        setOpen(true);
        setBookedByFullName(fullName);
        medicalbookingService.getBookingDetail(hos_schedule_time_id, booked_by).then(
            async response => {
                setAllMedicalQuestion(response.data);
            },
            error => { }
        );
    }

    const handleClose = () => {
        setOpen(false);
    };
    return <Box display="flex" flexDirection="column" gap={1}>
        <Box display="flex" flexDirection={{ xs: "column", md: "row" }} gap={1}>
            <TextField fullWidth select label="Dzongkhag" placeholder="Dzongkhag" size="small" required
                value={selectedDzongkhagId} onChange={handleDzongkhagChange}>
                {allDzongkhag && allDzongkhag.map((item, idx) => {
                    return (
                        <MenuItem key={idx} value={item.dzongkhagId}><span>{item.dzongkhagName}</span></MenuItem>
                    );
                })}
            </TextField>

            <TextField fullWidth select label="Hospital" placeholder="Hospital" size="small" required value={selectedHospitalId}
                onChange={handleHospitalChange}>
                {allHospitals && allHospitals.map((item, idx) => {
                    return (
                        <MenuItem key={idx} value={item.hospitalId}><span>{item.hospitalName}</span></MenuItem>
                    );
                })}
            </TextField>
        </Box>
        <Box display="flex" flexDirection={{ xs: "column", md: "row" }} gap={1}>
            <TextField fullWidth select label="Year" placeholder="Year" size="small" required
                value={selectedYear} onChange={handleYearChangeChange}>
                {allYear && allYear.map((item, idx) => {
                    return (
                        <MenuItem key={idx} value={item.year}><span>{item.year}</span></MenuItem>
                    );
                })}
            </TextField>
            <Button variant="outlined" onClick={handleSearch}>Search</Button>
        </Box>
        <br />
        <Box maxHeight="25vh">
            <Stack direction="row">
                <Box flex={1}>
                    <Box border={1}><Typography align="center">Available Dates</Typography></Box>
                    <Box border={1} maxHeight="20vh" sx={{ overflowY: "scroll" }}>
                        <List>
                            {availableDates && availableDates.map((item, idx) => {
                                return (
                                    <ListItemButton key={idx} value={item.appointment_date} onClick={() => getAvailableTimeSlots(item.hospital_schedule_date_id)}>
                                        {moment(item.appointment_date).format('MMM D, YYYY')}
                                    </ListItemButton>
                                );
                            })}
                        </List>
                    </Box>
                </Box>
                <Box flex={1}>
                    <Box border={1}><Typography align="center">Available Time</Typography></Box>
                    <Box border={1} maxHeight="20vh" sx={{ overflowY: "scroll" }}>
                        <List>
                            {availableTimeSlots && availableTimeSlots.map((item, idx) => {
                                const label = moment(item.start_time).format(timeFormat) + " - " + moment(item.end_time).format(timeFormat);
                                return (
                                    <ListItemButton key={idx}>
                                        {item.book_status === 'A' ? <span className="successMsg">{label}</span> :
                                            <div><span className="warningMsg">{label} </span><span
                                                onClick={() => getBookingDetail(item.hos_schedule_time_id, item.booked_by, item.fullName)}> Booked by {item.fullName}</span></div>}
                                    </ListItemButton>
                                );
                            })}
                        </List>
                    </Box>
                </Box>
            </Stack>
        </Box><br />

        <Dialog
            open={open}
            TransitionComponent={Transition}
            keepMounted
            onClose={handleClose}
            aria-labelledby="alert-dialog-slide-title"
            aria-describedby="alert-dialog-slide-description"
        >
            <DialogTitle id="alert-dialog-slide-title">{"Self-declaration"}</DialogTitle>
            <DialogContent>
                <div className="col-md-12 row">
                    <p>{bookedByFullName} has submitted following response</p>
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

            </DialogContent>
            <DialogActions>
                <Button color="secondary" variant="contained" onClick={handleClose}>
                    Cancel
                </Button>
            </DialogActions>
        </Dialog>
    </Box>;
}

export default AppointmentList;