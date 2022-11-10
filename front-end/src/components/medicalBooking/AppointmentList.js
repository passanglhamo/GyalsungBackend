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
// import Select from '@material-ui/core/Select';


const AppointmentList = () => {

    const [allDzongkhag, setAllDzongkhag] = useState([]);
    const [allHospitals, setAllHospitals] = useState([]);
    const [selectedDzongkhagId, setSelectedDzongkhagId] = useState('');
    const [selectedHospitalId, setSelectedHospitalId] = useState('');
    const [allYear, setAllYear] = useState([{ "year": 2022 }, { "year": 2023 }]);
    const [selectedYear, setSelectedYear] = useState('');

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

    const getAllBookingByHospitalIdAndYear = () => {
        medicalbookingService.getAllBookingByHospitalIdAndYear(selectedHospitalId, selectedYear).then(
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
        });
    }


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
            <Button variant="outlined">Search</Button>
        </Box>
        <br />
        <Box maxHeight="25vh">
            <Stack direction="row">
                <Box flex={1}>
                    <Box border={1}><Typography align="center">Available Dates</Typography></Box>
                    <Box border={1} maxHeight="20vh" sx={{ overflowY: "scroll" }}>
                        <List>
                            <ListItemButton>22nd Jan 2023</ListItemButton>
                            <ListItemButton>23rd Jan 2023</ListItemButton>
                            <ListItemButton>24th Jan 2023</ListItemButton>
                            <ListItemButton>25th Jan 2023</ListItemButton>
                            <ListItemButton>26th Jan 2023</ListItemButton>
                        </List>
                    </Box>
                </Box>
                <Box flex={1}>
                    <Box border={1}><Typography align="center">Available Time</Typography></Box>
                    <Box border={1} maxHeight="20vh" sx={{ overflowY: "scroll" }}>
                        <List>
                            <ListItemButton>09:15 AM</ListItemButton>
                            <ListItemButton>10:15 AM</ListItemButton>
                            <ListItemButton>11:15 AM</ListItemButton>
                            <ListItemButton>01:15 PM</ListItemButton>
                            <ListItemButton>02:15 PM</ListItemButton>
                            <ListItemButton>03:15 PM</ListItemButton>
                            <ListItemButton>04:15 PM</ListItemButton>
                        </List>
                    </Box>
                </Box>
            </Stack>
        </Box><br />
        {/* <Box><Button variant="outlined">Submit</Button></Box> */}
    </Box>;
}

export default AppointmentList;