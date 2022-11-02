
import React, { useState, useMemo, useEffect } from "react";
import { makeStyles } from "@material-ui/core/styles";
import Typography from '@material-ui/core/Typography';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import FormControl from "@material-ui/core/FormControl";
import InputLabel from "@material-ui/core/InputLabel";
import SearchIcon from '@material-ui/icons/Search';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import InputAdornment from '@material-ui/core/InputAdornment';
import Visibility from '@material-ui/icons/Visibility';
import VisibilityOff from '@material-ui/icons/VisibilityOff';
import IconButton from '@material-ui/core/IconButton';
import Input from '@material-ui/core/Input';
import Alert from '@material-ui/lab/Alert';
import CloseIcon from '@material-ui/icons/Close';
import Collapse from '@material-ui/core/Collapse';
import { useSelector } from "react-redux";
import FormHelperText from '@material-ui/core/FormHelperText';
import { useNavigate } from 'react-router-dom';
import { ArrowBack, ArrowForward, CheckCircle } from '@material-ui/icons';
import VpnKeyIcon from '@material-ui/icons/VpnKey';
import profileService from "../services/profile.service";

const useStyles = makeStyles((theme) => ({
    root: {
        padding: theme.spacing(0, 5),
        '& .MuiTextField-root': {
            width: '100%',
            borderRadius: '25px',
        },
    },
}));


const ResetUserPassword = () => {
    const classes = useStyles();
    const navigate = useNavigate();
    const { user: currentUser } = useSelector((state) => state.auth);
    const [newPassword, setNewPassword] = useState(undefined);
    const [confirmPassword, setConfirmPassword] = useState(undefined);
    const [errorMsg, setErrorMsg] = useState('');
    const [searchErrorMsg, setSearchErrorMsg] = useState('');
    const [searchSuccessful, setSearchSuccessful] = useState(false);
    const [successfull, setSuccessfull] = useState(false);
    const [loading, setLoading] = useState(false);
    const [submitLoading, setSubmitLoading] = useState(false);
    const [showAlert, setShowAlert] = useState(false);
    const [showRequiredErrorMsg, setShowRequiredErrorMsg] = useState(false);
    const [equiredErrorMsg, setRequiredErrorMsg] = useState('');

    const [activeStep, setActiveStep] = useState(0);
    const [searchKey, setSearchKey] = useState('');
    const [user, setUser] = useState([]);

    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [passwordMatch, setPasswordMatch] = useState(false);
    const [showSearchAlert, setShowSearchAlert] = useState(false);

    const handleNext = (e) => {
        setActiveStep((prevActiveStep) => prevActiveStep + 1);
    }
    const handleBack = (e) => {
        setActiveStep((prevActiveStep) => prevActiveStep - 1);
    }

    const resetAnother = (e) => {
        setSearchKey('');
        setUser('');
        setNewPassword('');
        setConfirmPassword('');
        setSearchSuccessful(false);
        setSuccessfull(false);
        setShowAlert(false);
        setPasswordMatch(true);
        setActiveStep((prevActiveStep) => prevActiveStep - 1);
    };

    const handlePasswordChange = (e) => {
        setNewPassword(e.target.value);
        if (confirmPassword.length > 0) {
            if (confirmPassword !== e.target.value) {
                setPasswordMatch(true);
            } else {
                setPasswordMatch(false);
            }
        }
    };

    const handleClickShowPassword = () => {
        setShowPassword(!showPassword);
    };


    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };

    const handleConfirmPasswordChange = (e) => {
        setConfirmPassword(e.target.value);
        if (newPassword.length > 0) {
            if (newPassword !== e.target.value) {
                setPasswordMatch(true);
            } else {
                setPasswordMatch(false);
            }
        }
    };

    const handleClickShowConfirmPassword = () => {
        setShowConfirmPassword(!showConfirmPassword);
    };

    const handleSearchUser = (e) => {
        if (searchKey === '') {
            setRequiredErrorMsg('This field is required');
            setShowRequiredErrorMsg(true);
        } else {
            setRequiredErrorMsg('');
            setShowRequiredErrorMsg(false);
            setLoading(true);
            setSearchSuccessful(false);
            profileService.searchUser(searchKey).then(
                response => {
                    setUser(response.data);
                    setLoading(false);
                    setShowSearchAlert(false);
                    setSearchSuccessful(true);
                },
                error => {
                    setLoading(false);
                    setSearchSuccessful(false);
                    setSearchErrorMsg(error.response.data.message);
                    setShowSearchAlert(true);
                }
            );
        }

    };

    const handleSubmit = () => {
        let userId = user.id;
        const data = { userId, newPassword, confirmPassword };
        if (!passwordMatch) {
            setSubmitLoading(true);
            profileService.resetUserPassword(data).then(
                response => {
                    setSubmitLoading(false);
                    setSuccessfull(true);
                    setShowAlert(true);
                },
                error => {
                    setSubmitLoading(false);
                    setSuccessfull(false);
                    setShowAlert(false);
                    setErrorMsg(error.response.data.message);
                }
            );
        }
    }

    return (
        <>
            <div className="col-md-12 row">
                <div className="d-flex flex-wrap flex-column align-items-center justify-content-center">
                    <div className="col-md-8 text-muted">
                        <div className="cards">
                            <div className="card-body">
                                <div className="col-md-12 row mb-2">
                                    <div className="d-flex flex-wrap flex-column align-items-center justify-content-center">
                                        <VpnKeyIcon style={{ fontSize: 60 }} />
                                        <Typography component="h1" variant="h6" color="inherit" noWrap>
                                            Reset user password
                                        </Typography>
                                    </div>
                                </div>
                                {successfull === true ? (
                                    <>
                                        <div className='text-center alert alert-success text-success animated bounceIn'>
                                            <h5><CheckCircle /> Confirmation!</h5>
                                            <strong> Password has been reset successfully.</strong>
                                        </div>
                                        <div className='d-flex justify-content-end animated fadeIn'>
                                            <Button variant="contained" color="primary"
                                                onClick={resetAnother} >Reset another user password</Button>
                                        </div>
                                    </>
                                ) : (
                                    <div>
                                        {activeStep === 0 ?
                                            <form className={classes.root}>
                                                <div className='col-md-12 row mb-2'>
                                                    <span>
                                                        Please enter the valid email address/CID/Username to reset password
                                                    </span>
                                                </div>
                                                <div className='mb-2'>
                                                    <TextField
                                                        id="standard-textarea"
                                                        label="Email/CID/Username"
                                                        required
                                                        error={showRequiredErrorMsg}
                                                        helperText={showRequiredErrorMsg === true ? equiredErrorMsg : ''}
                                                        value={searchKey}
                                                        onChange={(e) => { setSearchKey(e.target.value) }}
                                                    />
                                                </div>
                                                <div className='mb-2'>
                                                    <Button variant="contained" color="primary"
                                                        size="small"
                                                        onClick={handleSearchUser}
                                                        disabled={loading}>
                                                        {loading && (<span className="spinner-border spinner-border-sm"></span>)}
                                                        <SearchIcon /> Search user
                                                    </Button>
                                                </div>
                                                <hr></hr>
                                                <div hidden={!showSearchAlert} className="animated bounceIn">
                                                    <Collapse in={showSearchAlert}>
                                                        <Alert
                                                            severity='error'
                                                            action={
                                                                <IconButton
                                                                    aria-label="close"
                                                                    color="inherit"
                                                                    size="small"
                                                                    onClick={() => {
                                                                        setShowSearchAlert(false);
                                                                    }}
                                                                >
                                                                    <CloseIcon fontSize="inherit" />
                                                                </IconButton>
                                                            }
                                                        >
                                                            {searchErrorMsg}
                                                        </Alert>
                                                    </Collapse>
                                                </div>
                                                <div hidden={!searchSuccessful}>
                                                    <div className="d-flex justify-content-between align-items-center animated fadeIn">
                                                        <div className="col">
                                                            <div className="mb-2">
                                                                Full Name:
                                                                <strong className="text-muted"> {user.fullName}</strong>
                                                            </div>
                                                            <div className="mb-2">
                                                                Gender:
                                                                <strong className="text-muted"> {user.sex}</strong>
                                                            </div>
                                                            <div className="mb-2">
                                                                Mobile number:
                                                                <strong className="text-muted"> {user.mobileNo}</strong>
                                                            </div>
                                                        </div>
                                                        <div className="col-auto">
                                                            <Button variant="contained" color="secondary" size="small"
                                                                onClick={handleNext}>
                                                                Reset password for this user <ChevronRightIcon />
                                                            </Button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </form>
                                            :
                                            <form className={classes.root}>
                                                <div className="col-md-12 row text-muted mb-2 animated fadeIn">
                                                    <span className="mb-2">
                                                        Reset user password for {user.fullName}
                                                    </span>
                                                    <div className="col-md-6">
                                                        <FormControl>
                                                            <InputLabel htmlFor="standard-adornment-password">Password *</InputLabel>
                                                            <Input
                                                                id="standard-adornment-password"
                                                                required
                                                                error={passwordMatch}
                                                                type={showPassword ? 'text' : 'password'}
                                                                value={newPassword}
                                                                onChange={handlePasswordChange}
                                                                endAdornment={
                                                                    <InputAdornment position="end">
                                                                        <IconButton
                                                                            aria-label="toggle password visibility"
                                                                            onClick={handleClickShowPassword}
                                                                            onMouseDown={handleMouseDownPassword}
                                                                        >
                                                                            {showPassword ? <Visibility /> : <VisibilityOff />}
                                                                        </IconButton>
                                                                    </InputAdornment>
                                                                }
                                                            />
                                                        </FormControl>
                                                        <FormHelperText style={{ color: 'red' }}> {passwordMatch === true ? "Password missmatched" : ''}</FormHelperText>

                                                    </div>
                                                    <div className="col-md-6">
                                                        <FormControl>
                                                            <InputLabel htmlFor="standard-adornment-password">Confirm Password *</InputLabel>
                                                            <Input
                                                                error={passwordMatch}
                                                                id="standard-adornment-password"
                                                                type={showConfirmPassword ? 'text' : 'password'}
                                                                value={confirmPassword}
                                                                onChange={handleConfirmPasswordChange}
                                                                endAdornment={
                                                                    <InputAdornment position="end">
                                                                        <IconButton
                                                                            aria-label="toggle confirm password visibility"
                                                                            onClick={handleClickShowConfirmPassword}
                                                                            onMouseDown={handleMouseDownPassword}
                                                                        >
                                                                            {showConfirmPassword ? <Visibility /> : <VisibilityOff />}
                                                                        </IconButton>
                                                                    </InputAdornment>
                                                                }
                                                            />
                                                        </FormControl>
                                                        <FormHelperText style={{ color: 'red' }}> {passwordMatch === true ? "Password missmatched" : ''}</FormHelperText>
                                                    </div>
                                                </div>

                                                <div className='col-md-12 row'>
                                                    <hr></hr>
                                                    <div className='d-flex justify-content-end mb-2'>
                                                        <div className="col-md-2">
                                                            <Button variant="contained" color="secondary"
                                                                onClick={handleBack}
                                                            >
                                                                Cancel </Button>
                                                        </div>
                                                        <div className="col-md-3 p-0">
                                                            <Button variant="contained" color="primary" className="ml-2"
                                                                onClick={handleSubmit} disabled={submitLoading}>
                                                                {submitLoading && (<span className="spinner-border spinner-border-sm"></span>)}
                                                                Reset Now
                                                            </Button>
                                                        </div>
                                                    </div>

                                                    <div hidden={showAlert} className="animated bounceIn">
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
                                            </form>}
                                    </div>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}

export default ResetUserPassword;