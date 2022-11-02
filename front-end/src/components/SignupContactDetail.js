import React, { useState, useRef } from "react";

import { makeStyles } from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import FormHelperText from '@material-ui/core/FormHelperText';
import registerService from "../services/register.service";
import InputLabel from '@material-ui/core/InputLabel';
import InputAdornment from '@material-ui/core/InputAdornment';
import FormControl from '@material-ui/core/FormControl';
import Visibility from '@material-ui/icons/Visibility';
import VisibilityOff from '@material-ui/icons/VisibilityOff';
import IconButton from '@material-ui/core/IconButton';
import Input from '@material-ui/core/Input';

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
        '& .MuiFormControl-root': {
            width: '100%',
        },
    },
}));

const SignupContactDetail = () => {
    const classes = useStyles();

    const [mobileNo, setMobileNo] = useState(undefined);
    const [otp, setOtp] = useState(undefined);
    const [email, setEmail] = useState(undefined);
    const [verificationCode, setVerificationCode] = useState(undefined);
    const [password, setPassword] = useState(undefined);
    const [showPassword, setShowPassword] = useState(false);
    const [confirmPassword, setConfirmPassword] = useState(undefined);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [passwordMatch, setPasswordMatch] = useState(false);


    const [otpErrorMsg, setOtpErrorMsg] = useState(false);
    const [otpVerified, setOtpVerified] = useState(false);
    const [toggleOtpBtn, setToggleOtpBtn] = useState(true);
    const [loadingOtp, setLoadingOtp] = useState(false);
    const [receivedOtp, setReceivedOtp] = useState(undefined);
    const [loadingResendOtp, setLoadingResendOtp] = useState(false);

    const [vcodeErrorMsg, setVcodeErrorMsg] = useState(false);
    const [vcodeVerified, setVcodeVerified] = useState(false);
    const [toggleVcodeBtn, setToggleVcodeBtn] = useState(true);
    const [loadingVcode, setLoadingVcode] = useState(false);
    const [receivedVcode, setReceivedVcode] = useState(undefined);
    const [loadingResendVcode, setLoadingResendVcode] = useState(false);


    const [showEmailError, setShowEmailError] = useState(false);
    const [emailErrorMsg, setEmailErrorMsg] = useState('');



    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
        if (confirmPassword.length > 0) {
            if (confirmPassword !== e.target.value) {
                setPasswordMatch(true);
            } else {
                setPasswordMatch(false);
            }
        }
    };

    const handleConfirmPasswordChange = (e) => {
        setConfirmPassword(e.target.value);
        if (password.length > 0) {
            if (password !== e.target.value) {
                setPasswordMatch(true);
            } else {
                setPasswordMatch(false);
            }
        }
    };

    const handleClickShowPassword = () => {
        setShowPassword(!showPassword);
    };

    const handleClickShowConfirmPassword = () => {
        setShowConfirmPassword(!showConfirmPassword);
    };

    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };

    const handleMobileNo = (e) => {
        setMobileNo(e.target.value);
        console.log(mobileNo)
        if (e.target.value.length > 7) {
            setToggleOtpBtn(false);
        }

    }

    const handleEmail = (e) => {
        setEmail(e.target.value);
        if (e.target.value.length > 4) {
            setToggleVcodeBtn(false);
        }
    }

    const sendOtp = () => {
        setLoadingOtp(true);
        receiveOtp();
    }

    const receiveOtp = () => {
        const data = { mobileNo };
        registerService.receiveOtp(data).then(
            response => {
                setLoadingOtp(false);
                setLoadingResendOtp(false);
                setReceivedOtp(true);
            },
            error => {
                setLoadingOtp(false);
                setLoadingResendOtp(false);
                setReceivedOtp(false);
            }
        );
    }

    const resendOtp = () => {
        setLoadingResendOtp(true);
        receiveOtp();
    }

    const handleOptChange = (e) => {
        setOtp(e.target.value);
        if (otp.length === 4) {
            verifyOtp();
        }
    }

    const verifyOtp = () => {
        const data = { mobileNo, otp };
        registerService.verifyOtp(data).then(
            response => {
                setOtpVerified(true);
                setToggleOtpBtn(true);
                setOtpErrorMsg(false);
            },
            error => {
                setOtpVerified(false);
                setToggleOtpBtn(false);
                setOtpErrorMsg(true);//to show incorrect otp error msg 
            }
        );
    }

    const sendVcode = () => {
        setLoadingVcode(true);
        receiveVcode();
    }

    const receiveVcode = () => {
        const data = { email };
        registerService.receiveVcode(data).then(
            response => {
                setLoadingVcode(false);
                setLoadingResendVcode(false);
                setReceivedVcode(true);
                setEmailErrorMsg('');
                setShowEmailError(false);
            },
            error => {
                setLoadingVcode(false);
                setLoadingResendVcode(false);
                setReceivedVcode(false);
                setEmailErrorMsg(error.response.data.message);
                setShowEmailError(true);
            }
        );
    }

    const resendVcode = () => {
        setLoadingResendVcode(true);
        receiveVcode();
    }

    const handleVcodeChange = (e) => {
        setVerificationCode(e.target.value);
        if (verificationCode.length === 6) {
            verifyVcode();
        }
    }

    const verifyVcode = () => {
        const data = { email, verificationCode };
        registerService.verifyVcode(data).then(
            response => {
                setVcodeVerified(true);
                setToggleVcodeBtn(true);
                setVcodeErrorMsg(false);
            },
            error => {
                setVcodeVerified(false);
                setToggleVcodeBtn(false);
                setVcodeErrorMsg(true);//to show incorrect v code error msg 
            }
        );
    }

    return {
        mobileNo, otp, email, verificationCode, password, confirmPassword, contactDetail: (
            <div className='cards'>
                <div className='card-bodys p-4'>
                    <form className={classes.root} >
                        <div className='col-md-12 row'>
                            <div className='col-md-6'>
                                <div className='row'>
                                    <div className='col-md-8'>
                                        <TextField
                                            label="Mobile number"
                                            required
                                            disabled={otpVerified}
                                            value={mobileNo}
                                            onChange={handleMobileNo}
                                        // onChange={(e) => { setMobileNo(e.target.value) }}
                                        />
                                    </div>
                                    <div className='col-md-4'>
                                        <Button variant="contained" color="primary" className="animated fadeIn"
                                            disabled={loadingOtp || receivedOtp}
                                            hidden={toggleOtpBtn}
                                            onClick={sendOtp} >
                                            {loadingOtp && (<span className="spinner-border spinner-border-sm"></span>)}
                                            Send me OTP
                                        </Button>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="col-md-8">
                                        <TextField id="otp" label="OTP" required
                                            value={otp}
                                            error={otpErrorMsg}
                                            helperText={otpErrorMsg === true ? "OTP didn't match." : ''}
                                            disabled={otpVerified}
                                            onChange={handleOptChange} />
                                    </div>
                                </div>
                                <div className="col" hidden={otpVerified}>
                                    <FormHelperText>
                                        Enter 4-digit OTP sent to your mobile. <span>Didn't receive OTP?
                                            <Button variant="outline" color="primary"
                                                disabled={loadingResendOtp} onClick={resendOtp}>
                                                {loadingResendOtp && (<span className="spinner-border spinner-border-sm"></span>)}
                                                Resend OTP
                                            </Button>
                                        </span>
                                    </FormHelperText>
                                </div>
                            </div>
                            <div className='col-md-6'>
                                <div className='row'>
                                    <div className='col-md-7'>
                                        <TextField
                                            label="Email"
                                            required
                                            error={emailErrorMsg}
                                            helperText={showEmailError === true ? emailErrorMsg : ''}
                                            disabled={vcodeVerified}
                                            value={email}
                                            onChange={handleEmail}
                                        />
                                    </div>
                                    <div className='col-md-5'>
                                        <Button variant="contained" color="primary" className="animated fadeIn"
                                            disabled={loadingVcode || receivedVcode}
                                            hidden={toggleVcodeBtn}
                                            onClick={sendVcode} >
                                            {loadingVcode && (<span className="spinner-border spinner-border-sm"></span>)}
                                            Send Verification Code
                                        </Button>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="col-md-7">
                                        <TextField id="vcode" label="Verification Code" required
                                            value={verificationCode}
                                            error={vcodeErrorMsg}
                                            helperText={vcodeErrorMsg === true ? "Verification code didn't match." : ''}
                                            disabled={vcodeVerified}
                                            onChange={handleVcodeChange} />
                                    </div>
                                </div>

                                <div className="col" hidden={vcodeVerified}>
                                    <FormHelperText>
                                        Enter 6-digit Verification code sent to your email. <span>Didn't receive code?
                                            <Button variant="outline" color="primary"
                                                disabled={loadingResendVcode} onClick={resendVcode}>
                                                {loadingResendVcode && (<span className="spinner-border spinner-border-sm"></span>)}
                                                Resend OTP
                                            </Button>
                                        </span>
                                    </FormHelperText>
                                </div>


                            </div>
                        </div>


                        <div className='col-md-12 row'>
                            <div className='col-md-6'>
                                <FormControl>
                                    <InputLabel htmlFor="standard-adornment-password">Password *</InputLabel>
                                    <Input
                                        id="standard-adornment-password"
                                        required
                                        error={passwordMatch}
                                        type={showPassword ? 'text' : 'password'}
                                        value={password}
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
                                <FormHelperText>Strong password is highly recommended</FormHelperText>

                            </div>
                            <div className='col-md-6'>
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
                    </form>
                </div>
            </div>
        )
    }
}

export default SignupContactDetail;