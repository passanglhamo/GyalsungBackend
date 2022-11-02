import React, { useState, useMemo, useEffect } from "react";
import { useSelector } from "react-redux";
import Button from '@material-ui/core/Button';
import { makeStyles } from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import FormHelperText from '@material-ui/core/FormHelperText';
import { useNavigate } from 'react-router-dom';
import Typography from '@material-ui/core/Typography';
import ChevronLeftRoundedIcon from '@material-ui/icons/ChevronLeftRounded';
import profileService from "../../services/profile.service";
import { ArrowBack, ArrowForward, CheckCircle } from '@material-ui/icons';
import moment from 'moment';
import EditLocationOutlinedIcon from '@material-ui/icons/EditLocationOutlined';
import registerService from "../../services/register.service";
import Chip from '@material-ui/core/Chip';
import DateFnsUtils from '@date-io/date-fns';
import { MuiPickersUtilsProvider, KeyboardDatePicker, } from '@material-ui/pickers';
import Alert from '@material-ui/lab/Alert';
import IconButton from '@material-ui/core/IconButton';
import Collapse from '@material-ui/core/Collapse';
import CloseIcon from '@material-ui/icons/Close';

const useStyles = makeStyles((theme) => ({
    root: {
        '& .MuiFormControl-root': {
            width: '100%',
            // margin: theme.spacing(1)
        }
    },
    formControl: {
        margin: theme.spacing(1),
        minWidth: '100%',
    },
    generalSetting: {
        width: '100%',
        maxWidth: 500,
    },
}));


const EditSyncCensusRecord = () => {
    const classes = useStyles();
    const navigate = useNavigate();
    const { user: currentUser } = useSelector((state) => state.auth);

    const [cidNumber, setCidNumber] = useState('');
    const [dateOfBirth, setDateOfBirth] = useState(null);//when set to null default is empty in datepicker

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
    const [showAlert, setShowAlert] = useState(false);

    const [cidErrorMsg, setCidErrorMsg] = useState(false);
    const [dobErrorMsg, setDobErrorMsg] = useState(false);

    const [errorMsg, setErrorMsg] = useState('');
    const [successfull, setSuccessfull] = useState(false);
    const [loading, setLoading] = useState(false);
    const [loadingCheck, setLoadingCheck] = useState(false);
    const [submitErrorMsg, setSubmitErrorMsg] = useState('');
    const [showSubmitAlert, setShowSubmitAlert] = useState(false);

    let userId = currentUser.userId;

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
        setLoadingCheck(true);
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
                setLoadingCheck(false);
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
                setLoadingCheck(false);
                setSuccessful(false);
                setErrorMsg(error.response.data.message);
                setShowAlert(true);
                console.log(
                    (error.response && error.response.data && error.response.data.message) ||
                    error.message || error.toString()
                );
            }
        );
    }


    const handleSubmit = (e) => {
        setLoading(true);
        let birthDate = dob;
        let sex = gender;
        const data = {
            userId, cid, birthDate, fullName, sex, fatherName, motherName, permanentPlaceName,
            permanentGeog, permanentDzongkhag
        };
        profileService.syncCensusRecord(data).then(
            response => {
                //show success message
                setSuccessfull(true);
                setLoading(false);
                setShowSubmitAlert(false);
                setSubmitErrorMsg('');
            },
            error => {
                //show error message 
                setLoading(false);
                setSuccessfull(false);
                setShowSubmitAlert(true);
                setSubmitErrorMsg(error.response.data.message);
            }
        );
    }

    return (
        <>
            <div className="col-md-12 row mb-2">
                <div className="col-md-4">
                    <Button variant="contained" color="primary" size="small" onClick={() => navigate("/gyalsupProfile")}>
                        <ChevronLeftRoundedIcon /> Go back </Button>
                </div>
            </div>
            <div className="d-flex flex-wrap flex-column align-items-center justify-content-center">
                <div className="col-md-11 row text-muted">
                    <div className="cards">
                        <div className="card-body">
                            <div className="col-md-12 row mb-2">
                                <div className="d-flex flex-wrap flex-column align-items-center justify-content-center">
                                    <EditLocationOutlinedIcon style={{ fontSize: 60 }} />
                                    <Typography component="h1" variant="h6" color="inherit" noWrap>
                                        Update
                                    </Typography>
                                </div>
                            </div>
                            {successfull === true ? (
                                <div className='text-center alert alert-success text-success animated bounceIn'>
                                    <h5><CheckCircle /> Confirmation!</h5>
                                    <strong> Your record has been changed successfully.</strong>
                                </div>
                            ) : (
                                <form className={classes.root}>
                                    <div className='col-md-12 row mb-2'>
                                        <span>
                                            If you have changed anything in national census record and not updated here you are required to
                                            update. First you need to check census data by entering correct CID and Date of Birth.
                                        </span>
                                    </div>
                                    <div className='col-md-12 row mb-4'>
                                        <div className='col-md-4'>
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

                                        <div className='col-md-3'>
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
                                        <div className='col-md-5'>
                                            <Button variant="contained" color="primary"
                                                onClick={() => { checkMyDetail(); }}
                                                disabled={loadingCheck} >
                                                {loadingCheck && (<span className="spinner-border spinner-border-sm"></span>)} Check my detail
                                            </Button>
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
                                    </div>
                                    <div className="col-md-12 row animated fadeIn" hidden={!successful}>
                                        <div className="col-md-4">
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
                                        <div className="col-md-4">
                                            <div className="mb-2">
                                                Father's name: <strong>{fatherName}</strong>
                                            </div>
                                            <div className="mb-2">
                                                Mother's name: <strong>{motherName}</strong>
                                            </div>
                                        </div>
                                        <div className="col-md-4">
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
                                        <div className="mb-2">
                                            <mark>Note: Above information will be updated to the system. Please confirm before you proceed to update.</mark>
                                        </div>
                                        <div className='d-flex flex-wrap flex-column align-items-center mb-2'>
                                            <Button variant="contained" color="primary" onClick={handleSubmit} disabled={loading}>
                                                {loading && (<span className="spinner-border spinner-border-sm"></span>)}
                                                Update Now
                                            </Button>
                                        </div>
                                        <div className='mb-2'>
                                            <Collapse in={showSubmitAlert}>
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
                                                    {submitErrorMsg}
                                                </Alert>
                                            </Collapse>
                                        </div>
                                    </div>
                                </form>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}

export default EditSyncCensusRecord;