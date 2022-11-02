import React, {useEffect, useState} from "react";
import hospitalScheduleTimeService from "../../services/hospitalScheduleTime.service";
import {MenuItem, TextField} from "@material-ui/core";
import hospital from "../../data/hospital.json";
import dzongkhagHospitalMapService from "../../services/dzongkhagHospitalMap.service";
import FormControl from "@material-ui/core/FormControl";
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import {Paper, Button} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import Chip from '@mui/material/Chip';
import moment from 'moment';
import Carousel from 'react-material-ui-carousel';
import Grid from '@mui/material/Grid';
import ModeEditIcon from '@mui/icons-material/ModeEdit';
import Box from '@mui/material/Box';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';

import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import {createTheme, ThemeProvider, styled} from '@mui/material/styles';
import Typography from '@mui/material/Typography';
import DateFnsUtils from "@date-io/date-fns";
import {KeyboardTimePicker, MuiPickersUtilsProvider} from "@material-ui/pickers";
import hospitalService from "../../services/hospital.service";

const useStyles = makeStyles((theme) => ({
    root: {
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        padding: theme.spacing(2),

        '& .MuiTextField-root': {
            margin: theme.spacing(1),
            width: '100%',
        },
        '& .MuiButtonBase-root': {
            margin: theme.spacing(2),
        },
    },
    formControl: {
        margin: theme.spacing(1),
        minWidth: '100%',
    },
}));
const BootstrapDialog = styled(Dialog)(({theme}) => ({
    '& .MuiDialogContent-root': {
        padding: theme.spacing(2),
    },
    '& .MuiDialogActions-root': {
        padding: theme.spacing(1),
    },
}));

const ItemStyle = styled(Paper)(({theme}) => ({
    ...theme.typography.body2,
    textAlign: 'center',
    color: theme.palette.text.secondary,
    height: 70,
    lineHeight: '70px',
}));


const HospitalScheduleTimeList = () => {

    /*================ constants value =============*/
    const dateFormat = "MMMM DD, YYYY";
    const timeFormat = "hh:mm A";
    const classes = useStyles();
    const [hospitalScheduleTimeList, setHospitalScheduleTimeList] = React.useState([]);
    const [content, setContent] = useState('');
    const [dzoHospitalMappingList, setDzoHospitalMappingList] = useState([]);
    const [dzoHosId, setDzoHosId] = useState(null);
    const [uniqueDates, setUniqueDates] = useState([]);
    const [open, setOpen] = useState(false);
    const [scheduleTimeList, setScheduleTimeList] = useState([]);
    const [startTime, setStartTime] = useState(null);
    const [endTime, setEndTime] = useState(null);
    const [id, setId] = useState('');
    const [hospitalList, setHospitalList] = useState([]);
    const [activeStatus, setActiveStatus] = useState('A');



    const handleHospital = (items) => {
        setDzoHosId(items.hospitalId);
    };


    useEffect(() => {
        getHospitalList();
        getDzoHosMapList();

    }, [])

    const handleStartTime = (time) => {
        setStartTime(time);
    };
    const handleEndTime = (time) => {
        setEndTime(time);
    };

    const handleClose = () => {
        setOpen(false);
    };

    // var items = [
    //     {
    //         name: "Random Name #1",
    //         description: "Probably the most random thing you have ever seen!"
    //     },
    //     {
    //         name: "Random Name #2",
    //         description: "Hello World!"
    //     }
    // ]
    /*================ Edit method =============*/
    const handleEdit = () => {
        const data = {id, startTime, endTime};
        hospitalScheduleTimeService.updateScheduleTimes(data)
            .then(() => {

            })
            .catch(() => {
            });

    };

    /*================ Get All Hospital method =============*/
    const getHospitalList = () => {
        hospitalService.getAll().then(
            response => {
                setHospitalList(response.data)
            },
            error => {
                setContent(
                    (error.response &&
                        error.response.data &&
                        error.response.data.message) ||
                    error.message ||
                    error.toString()
                );
            }
        );

    }
    /*================Method to get hospital list =============*/
    const getDzoHosMapList = () => {
        dzongkhagHospitalMapService.getAllByStatus(activeStatus).then(
            async response => {

                setDzoHospitalMappingList(response.data);
            },
            error => {
                setContent(
                    (error.response &&
                        error.response.data &&
                        error.response.data.message) ||
                    error.message ||
                    error.toString()
                );
            }
        )
    }

    /*================Method to get schedule times for selected hospital =============*/
    const handleGetListByHospital = () => {
        hospitalScheduleTimeService.getAllScheduleTimesById(dzoHosId).then(
            async response => {

                setHospitalScheduleTimeList(response.data);
                let responseDates = [];
                response.data.filter(element => {
                    const isDuplicate = responseDates.includes(moment(element.appointmentDate).format(dateFormat));

                    if (!isDuplicate) {
                        responseDates.push(moment(element.appointmentDate).format(dateFormat));

                        return true;
                    }

                    return false;
                });

                setUniqueDates(responseDates);
                handleTimeList(responseDates[0]);
            },
            error => {
                setContent(
                    (error.response &&
                        error.response.data &&
                        error.response.data.message) ||
                    error.message ||
                    error.toString()
                );
            }
        )
    }
    /*================Method to get list of available appointment times for the date=============*/
    const handleTimeList = (active) => {
        let scheduleList = [];
        let scheduleTimeList = [];
        scheduleTimeList.push(hospitalScheduleTimeList.filter(hospitalScheduleTime =>
            moment(hospitalScheduleTime.appointmentDate).format(dateFormat) === uniqueDates[active])
        );

        scheduleTimeList.map(timeList => {
            timeList[0].hospitalScheduleTimeListDtos.map(timeListItem => {
                scheduleList.push(timeListItem);

                return true;
            });
            setScheduleTimeList(scheduleList)
            return true;
        })

    }

    function Item(props) {
        return (
            <div>
                <ItemStyle>
                {props.item}
                </ItemStyle>
            </div>



        )
    }


    /*================ Delete method =============*/
    const handleDelete = (id) => {
        hospitalScheduleTimeService.deleteScheduleTimes(id)
            .then(() => {

            })
            .catch(() => {
            });
    }

    return (
        <>
            <FormControl className={classes.formControl}>
                <div className="col-md-12 row">
                    {/*<Card sx={{display: 'flex'}}>*/}
                    {/*    <CardContent sx={{flex: '1 0 auto'}}>*/}
                            <div className="col-md-12 row">
                                <div className="col-md-5 offset-3 row">
                                    <TextField select={true} variant='standard' required name="dzoHosId"
                                               fullWidth={true} label="Hospital">
                                        {dzoHospitalMappingList.map((items) => {
                                            return (
                                                <MenuItem
                                                    onClick={(e) => handleHospital(items)}
                                                    key={items.hospitalId}
                                                    value={items.hospitalId}>
                                                    {hospitalList.find(hos => hos.hospitalId == items.hospitalId).hospitalName}
                                                </MenuItem>
                                            )
                                        })
                                        }
                                    </TextField>

                                </div>
                                <div className="col-md-1 d-flex justify-content-between align-items-center  row">
                                    <Button variant="contained" size="small" onClick={handleGetListByHospital}
                                            color="primary">
                                        Search
                                    </Button>
                                </div>

                            </div>

                            <div className="col-md-12 row">

                                <div className="col-md-12 row">
                                    <Carousel autoPlay={false} next={ ( next) =>  handleTimeList(next) }
                                        prev={ ( active) =>  handleTimeList(active) }>

                                        {
                                            uniqueDates.map((item, i) => <Item key={i} item={item} />)
                                        }
                                    </Carousel>

                                </div>

                            </div>


                            <div className="col-md-12 row">
                                <div className="col-md-12 row">
                                    <Paper>
                                    {
                                        scheduleTimeList.map((data, index) => {
                                            const label = moment(data.startTime).format(timeFormat) + "-" +
                                                moment(data.endTime).format(timeFormat);
                                            return (
                                                <div>
                                                    <Chip
                                                        label={label}
                                                        color="primary"
                                                        onDelete={() => handleDelete(data.id)}
                                                        icon={<ModeEditIcon onClick={() => {
                                                            setId(data.id)
                                                            setStartTime(data.startTime)
                                                            setEndTime(data.endTime)
                                                            setOpen(true);
                                                        }}/>}
                                                    />

                                                </div>
                                            );
                                        })}
                                    </Paper>
                                </div>
                            </div>
                    {/*    </CardContent>*/}
                    {/*</Card>*/}
                </div>
            </FormControl>
            <BootstrapDialog
                onClose={handleClose}
                aria-labelledby="customized-dialog-title"
                open={open}
            >
                <form className={classes.root}>
                    <DialogTitle id="customized-dialog-title" onClose={handleClose}>
                        Hospital Schedule Time
                    </DialogTitle>
                    <DialogContent dividers>
                        <Typography gutterBottom>
                            <MuiPickersUtilsProvider utils={DateFnsUtils}>
                                <KeyboardTimePicker
                                    required
                                    name="startTime"
                                    value={startTime}
                                    label="Start Time"
                                    onChange={e => handleStartTime(e)}
                                />
                            </MuiPickersUtilsProvider>
                        </Typography>
                        <Typography gutterBottom>
                            <MuiPickersUtilsProvider utils={DateFnsUtils}>
                                <KeyboardTimePicker
                                    required
                                    name="endTime"
                                    value={endTime}
                                    label="End Time"
                                    onChange={e => handleEndTime(e)}
                                />
                            </MuiPickersUtilsProvider>
                        </Typography>

                    </DialogContent>
                    <DialogActions>
                        <Button type="button" onClick={handleClose} variant='contained' color='primary'>
                            Cancel
                        </Button>
                        <Button variant="contained" color="primary" onClick={handleEdit}>
                            Update
                        </Button>
                    </DialogActions>
                </form>
            </BootstrapDialog>

        </>
    )

}

export default HospitalScheduleTimeList