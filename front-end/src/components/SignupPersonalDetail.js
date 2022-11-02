import 'date-fns';
import React, { useState } from 'react';
import Button from '@material-ui/core/Button';
import { makeStyles } from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import moment from 'moment';
import FormControl from '@material-ui/core/FormControl';
import InputLabel from '@material-ui/core/InputLabel';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import DateFnsUtils from '@date-io/date-fns';
import { MuiPickersUtilsProvider, KeyboardDatePicker, } from '@material-ui/pickers';
import Alert from '@material-ui/lab/Alert';
import IconButton from '@material-ui/core/IconButton';
import Collapse from '@material-ui/core/Collapse';
import CloseIcon from '@material-ui/icons/Close';
import Chip from '@material-ui/core/Chip';
import registerService from "../services/register.service";

const useStyles = makeStyles((theme) => ({
    root: {
        '& .MuiTextField-root': {
            // margin: theme.spacing(1),
            width: '100%',
        },
    },
    formControl: {
        // margin: theme.spacing(1),
        minWidth: '100%',
    },

    genderFormControl: {
        // margin: theme.spacing(1),
        minWidth: '100%',
        marginTop: '-5px',
    },
}));

const SignupPersonalDetail = () => {

    const classes = useStyles();

    const [cidNumber, setCidNumber] = useState('');
    const [dateOfBirth, setDateOfBirth] = useState(null);//when set to null default is empty in datepicker

    const [noCensus, setNoSencus] = useState(false);
    const [cid, setCid] = useState(undefined);
    const [dob, setDob] = useState(null);
    const [fullName, setFullName] = useState(undefined);
    const [gender, setGender] = useState(undefined);
    const [fatherName, setFatherName] = useState(undefined);
    const [fatherCid, setFatherCid] = useState(undefined);
    const [motherName, setMotherName] = useState(undefined);
    const [motherCid, setMotherCid] = useState(undefined);
    const [permanentPlaceName, setPermanentPlaceName] = useState(undefined);
    const [permanentGeog, setPermanentGeog] = useState(undefined);
    const [permanentDzongkhag, setPermanentDzongkhag] = useState(undefined);
    const [successful, setSuccessful] = useState(false);
    const [loading, setLoading] = useState(false);
    const [errorMsg, setErrorMsg] = useState(undefined);
    const [showAlert, setShowAlert] = useState(false);

    const [cidErrorMsg, setCidErrorMsg] = useState(false);
    const [dobErrorMsg, setDobErrorMsg] = useState(false);


    const handleCidChange = (e) => {
        setCidNumber(e.target.value);
        if (e.target.value.length > 0) {
            setCidErrorMsg(false);
        } else {
            setCidErrorMsg(true);
        }
    };

    const handleDobChange = (date) => {
        setDateOfBirth(date);

        if (date !== null) {
            setDobErrorMsg(false);
        } else {
            setDobErrorMsg(true);
        }
    };
    const handleNoCidDobChange = (date) => {
        setDob(date);
    };

    const validateFields = () => {
        if (cidNumber.trim() === '') {
            setCidErrorMsg(true);
        } else {
            setCidErrorMsg(false);
        }

        if (dateOfBirth === null) {
            setDobErrorMsg(true);
        } else {
            setDobErrorMsg(false);
        }
    };

    const checkMyDetail = () => {
        validateFields();
        if (cidNumber.trim() !== '' && dateOfBirth !== null) {
            getCitizenDetail();
        }
    }

    const getCitizenDetail = () => {
        setLoading(true);
        registerService.getCitizenDetail(cidNumber, moment(dateOfBirth).format('DD/MM/YYYY')).then(
            response => {
                setFullName(response.data.fullName);
                setGender(response.data.gender);
                setFatherName(response.data.fatherName);
                setFatherCid(response.data.fatherCid);
                setMotherName(response.data.motherName);
                setMotherCid(response.data.motherCid);
                setPermanentPlaceName(response.data.villageName);
                setPermanentGeog(response.data.geogName);
                setPermanentDzongkhag(response.data.dzongkhagName);
                setCid(response.data.cid);
                setDob(response.data.dob);
                setLoading(false);
                setSuccessful(true);
                setShowAlert(false);
            },
            error => {
                setFullName(undefined);
                setGender(undefined);
                setFatherName(undefined);
                setFatherCid(undefined);
                setMotherName(undefined);
                setMotherCid(undefined);
                setPermanentPlaceName(undefined);
                setPermanentGeog(undefined);
                setPermanentDzongkhag(undefined);
                setCid(undefined);
                setDob(undefined);
                setLoading(false);
                setSuccessful(false);
                setErrorMsg(error.response.data.message);
                setShowAlert(true);
                //set all personal detail(fullName, cid, gender, dob, etc.) to undefined
                console.log(
                    (error.response && error.response.data && error.response.data.message) ||
                    error.message || error.toString()
                );
            }
        );
    }

    const signupWitoutCid = () => {
        setNoSencus(!noCensus);
    }

    return {
        cid, dob, fullName, gender, fatherName, fatherCid, motherName, motherCid, permanentPlaceName,
        permanentGeog, permanentDzongkhag, personalDetail: (
            <div className='cards'>
                <div className={classes.root}>
                    {noCensus ? (
                        <div>
                            <div className="d-flex flex-wrap flex-column align-items-center justify-content-center">
                                <div className="mb-2">
                                    <Chip
                                        label=" You are atempting to sinup with no CID. Pleas signup only if you don't have 
                                        CID in the census record."
                                        color="secondary"
                                        variant="outlined"
                                    />
                                    <span onClick={signupWitoutCid}> Click here if you have CID</span>
                                </div>

                                <div className="col-md-10 row mb-2">
                                    <div className='col-md-5'>
                                        <FormControl className={classes.formControl}>
                                            <TextField
                                                id="standard-textarea"
                                                label="Full Name"
                                                required
                                                value={fullName}
                                                onChange={(e) => { setFullName(e.target.value) }}
                                            />
                                        </FormControl>
                                    </div>

                                    <div className='col-md-4'>
                                        <FormControl className={classes.formControl}>
                                            <MuiPickersUtilsProvider utils={DateFnsUtils}>
                                                <KeyboardDatePicker
                                                    margin="normal"
                                                    id="date-picker-dialog"
                                                    label="DOB"
                                                    required
                                                    format="dd/MM/yyyy"
                                                    placeholder="dd/MM/yyyy"
                                                    value={dob}
                                                    onChange={handleNoCidDobChange}
                                                    KeyboardButtonProps={{
                                                        'aria-label': 'change date',
                                                    }}
                                                />
                                            </MuiPickersUtilsProvider>
                                        </FormControl>
                                    </div>
                                    <div className='col-md-3'>
                                        <FormControl className={classes.genderFormControl}>
                                            <InputLabel id="demo-simple-select-helper-label">Gender</InputLabel>
                                            <Select labelId="demo-simple-select-helper-label">
                                                <MenuItem value={'MALE'}><span>Male</span></MenuItem>
                                                <MenuItem value={'FEMALE'}><span>Female</span></MenuItem>
                                            </Select>
                                        </FormControl>
                                    </div>
                                </div>

                                <div className="col-md-10 row mb-3">
                                    <div className='col-md-5'>
                                        <TextField
                                            id="standard-textarea"
                                            label="Father's name"
                                            required
                                            value={fatherName}
                                            onChange={(e) => { setFatherName(e.target.value) }}
                                        />
                                    </div>

                                    <div className='col-md-5'>
                                        <TextField
                                            id="standard-textarea"
                                            label="Mother's name"
                                            required
                                            value={motherName}
                                            onChange={(e) => { setMotherName(e.target.value) }}
                                        />
                                    </div>
                                </div>
                                <div className="col-md-10 row mb-2">
                                    <div className='col-md-4'>
                                        <TextField
                                            id="standard-textarea"
                                            label="Village"
                                            required
                                            value={permanentPlaceName}
                                            onChange={(e) => { setPermanentPlaceName(e.target.value) }}
                                        />
                                    </div>

                                    <div className='col-md-4'>
                                        <TextField
                                            id="standard-textarea"
                                            label="Geog"
                                            required
                                            value={permanentGeog}
                                            onChange={(e) => { setPermanentGeog(e.target.value) }}
                                        />
                                    </div>
                                    <div className='col-md-4'>
                                        <TextField
                                            id="standard-textarea"
                                            label="Dzongkhag"
                                            required
                                            value={permanentDzongkhag}
                                            onChange={(e) => { setPermanentDzongkhag(e.target.value) }}
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>
                    ) : (
                        <div>
                            <div className='col-md-12 row'>
                                <div className='col-md-4 offset-1'>
                                    <TextField
                                        error={cidErrorMsg}
                                        helperText={cidErrorMsg === true ? "CID is required" : ''}
                                        id="standard-textarea"
                                        label="CID"
                                        required
                                        value={cidNumber}
                                        onChange={handleCidChange}
                                    />
                                </div>

                                <div className='col-md-2'>
                                    <MuiPickersUtilsProvider utils={DateFnsUtils}>
                                        <KeyboardDatePicker
                                            error={dobErrorMsg}
                                            helperText={dobErrorMsg === true ? "DOB is required" : ''}
                                            margin="normal"
                                            id="date-picker-dialog"
                                            label="DOB"
                                            required
                                            format="dd/MM/yyyy"
                                            placeholder="dd/MM/yyyy"
                                            value={dateOfBirth}
                                            onChange={handleDobChange}
                                            KeyboardButtonProps={{
                                                'aria-label': 'change date',
                                            }}
                                        />
                                    </MuiPickersUtilsProvider>
                                </div>
                                <div className='col-md-2'>
                                    <Button variant="contained" color="primary"
                                        className={classes.backButton}
                                        onClick={() => {
                                            checkMyDetail();
                                        }}
                                        disabled={loading}
                                    >  {loading && (
                                        <span className="spinner-border spinner-border-sm"></span>
                                    )} Check my detail </Button>
                                </div>
                                {/* <div className='col-md-3'>
                                    Don't have census record? <span onClick={signupWitoutCid} className="cursor-pointer">Click here </span> to signup if you don't have CID.
                                </div> */}
                            </div>
                            <div className="animated bounceIn" hidden={!showAlert}>
                                <Collapse in={showAlert}>
                                    <Alert
                                        severity='error'
                                        action={
                                            <IconButton
                                                aria-label="close"
                                                color="inherit"
                                                size="small"
                                                onClick={() => {
                                                    setShowAlert(false);
                                                }}
                                            >
                                                <CloseIcon fontSize="inherit" />
                                            </IconButton>
                                        }
                                    >
                                        {errorMsg}
                                    </Alert>
                                </Collapse>
                            </div>

                            <div className='mb-4'></div>
                            <div className='dropdown-divider'></div>

                            <div className="col-md-12 row">
                                <div className="col-md-4">
                                    <div className={`card ${successful ? "bg-light-green" : ""}`}>
                                        <div className="card-body">
                                            <div className="mb-2">
                                                Full name: <strong>{fullName}</strong>
                                            </div>
                                            <div className="mb-2">
                                                Gender: <strong>{gender}</strong>
                                            </div>
                                            <div className="mb-2">
                                                CID: <strong>{cid}</strong>
                                            </div>

                                            <div className="mb-2">
                                                Birth date: <strong>{dob}</strong>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-md-4">
                                    <div className={`card ${successful ? "bg-light-green" : ""}`}>
                                        <div className="card-body">
                                            <div className="mb-2">
                                                Father's name: <strong>{fatherName}</strong>
                                            </div>
                                            <div className="mb-2">
                                                Father's CID: <strong>{fatherCid}</strong>
                                            </div>
                                            <div className="mb-2">
                                                Mother's name: <strong>{motherName}</strong>
                                            </div>

                                            <div className="mb-2">
                                                Mother's CID: <strong>{motherCid}</strong>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-md-4">
                                    <div className={`card ${successful ? "bg-light-green" : ""}`}>
                                        <div className="card-body">
                                            <div className="mb-2">
                                                Village: <strong>{permanentPlaceName}</strong>
                                            </div>
                                            <div className="mb-2">
                                                Geog: <strong>{permanentGeog}</strong>
                                            </div>
                                            <div className="mb-2">
                                                Dzongkhag: <strong>{permanentDzongkhag}</strong>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    )
                    }
                </div>
            </div>
        )
    }
}

export default SignupPersonalDetail;