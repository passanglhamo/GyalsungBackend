import React, { useState, useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link, useNavigate } from 'react-router-dom';
import { ArrowForward } from '@material-ui/icons';
import CheckButton from "react-validation/build/button";
import { login } from "../actions/auth";
import { makeStyles } from '@material-ui/core';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import Form from "react-validation/build/form";
import Alert from '@material-ui/lab/Alert';
import IconButton from '@material-ui/core/IconButton';
import Collapse from '@material-ui/core/Collapse';
import CloseIcon from '@material-ui/icons/Close';

const useStyles = makeStyles(theme => ({
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
}));


const Signin = () => {
    const classes = useStyles();

    let navigate = useNavigate();
    const form = useRef();
    const checkBtn = useRef();

    const [passwordShown, setPasswordShown] = useState(false);
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);
    const [showAlert, setShowAlert] = useState(false);

    const { isLoggedIn } = useSelector(state => state.auth);
    const { message } = useSelector(state => state.message);

    const dispatch = useDispatch();

    const showPassword = () => {
        setPasswordShown(!passwordShown);
    }

    const handleLogin = (e) => {
        e.preventDefault();
        setLoading(true);
        form.current.validateAll();
        if (checkBtn.current.context._errors.length === 0) {
            dispatch(login(username, password))
                .then(() => {
                    navigate("/gyalsupProfile");
                    window.location.reload();
                })
                .catch(() => {
                    setLoading(false);
                    setShowAlert(true);
                });
        } else {
            setLoading(false);
        }
    };

    if (isLoggedIn) {
        // return <Link to="/gyalsupProfile" />;
        navigate("/gyalsupProfile");
    }

    return (
        <div>
            <div className="col-lg-12">
                <div className="card">
                    <div className="card-body login-bg">
                        <div className="row align-items-center">
                            <div className="col-7">
                                <div className='mb-3'>
                                    <span style={{ color: '#fff' }} >
                                        <p>The National Service of Bhutan is a broad and profound concept which will have a transformative effect on the youth providing them with the opportunity to actualize their innate potential and unleash it in the service of Tsa-Wa-Sum.

                                            The Constitution of Kingdom of Bhutan, Article 8 on the Fundamental Duties states that a Bhutanese Citizen shall render National Service when called upon to do so by the Parliament. The Gyalsung Bill reaffirms the National Service Obligation whereby all 18 years Bhutanese youth must undergo Gyalsung Training, provide National Service Duty and serve as National Service Reservist until the age of 45.

                                            Considering the importance of the Gyalsung Program,vigorous sensitization and awareness programs will be carried out at all levels (schools, organizations, LG, etc) and through media channels.</p>
                                        <p>As per the Gyalsung Act of the Kingdom of Bhutan, a person shall be required to enlist for Gyalsung Training upon attaining at least the age of 18 years old. The Gyalsung Headquarter will notify eligible youth on their Gyalsung opportunity/obligation upon attaining 15 years old. The notification will be sent via letter/e-mail/SMS ,the same notification will also be sent to parents or legal guardians. The notification will entail information on the upcoming Gyalsung Obligation and require them to update their personal information including phone number , addresses and contact information through the Gyalsung portal.</p>
                                    </span>
                                </div>

                                <div className=''>
                                    <a href='https://training.desuung.org.bt/' className='btn btn-primary btn-sm' target='_balnk'> Read more  <ArrowForward /> </a>
                                </div>
                            </div>
                            <div className="col">
                                <div className="card">
                                    <div className="card-body">
                                        <div className='text-center p-1'>
                                            <h5 className='text-muted'> Please log in to continue</h5>
                                        </div>
                                        <Form className={classes.root} onSubmit={handleLogin} ref={form}>
                                            <TextField
                                                label="Username"
                                                variant="filled"
                                                required
                                                value={username}
                                                onChange={e => setUsername(e.target.value)}
                                            />
                                            <TextField
                                                label="Password"
                                                variant="filled"
                                                required
                                                type={passwordShown ? "text" : "password"}
                                                value={password}
                                                onChange={e => setPassword(e.target.value)}
                                            />
                                            <div className='col-md-12'>
                                                <div className='d-flex justify-content-between'>
                                                    <small className='pr-3 cursor-pointer' onClick={showPassword}><input type="checkbox" id="showPw" /> <label for="showPw" className="cursor-pointer">Show password</label></small>
                                                    <Link to='/forgotPassword'> <small className='pull-right'> Forgot password?</small></Link>
                                                </div>
                                            </div>
                                            <div className='col-md-12'>
                                                <div className='d-flex justify-content-between'>
                                                    <Button type="submit" variant="outlined" color="primary"
                                                        className="w-100" disabled={loading}>
                                                        {loading && (
                                                            <span className="spinner-border spinner-border-sm"></span>
                                                        )} LOG IN
                                                    </Button>
                                                </div>
                                            </div>

                                            {/* <div className='col-md-12 row'>
                                                {showAlert && (
                                                    <div className="alert alert-danger alert-dismissible" role="alert">
                                                        <div className="d-flex">
                                                            <div>
                                                                {message}
                                                            </div>
                                                        </div>
                                                        <a className="btn-close cursor-pointer" onClick={() => setShowAlert(false)}></a>
                                                    </div>
                                                )} 
                                            </div> */}
                                            <CheckButton style={{ display: "none" }} ref={checkBtn} />
                                        </Form>
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
                                                {message}
                                            </Alert>
                                        </Collapse>
                                        <div className='d-flex justify-content-end'>
                                            <div className='mb-2'>
                                                <small>Don't have an account yet?  </small>
                                                <Link to='/signup'> <small className='pull-right'>Signup here</small></Link>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Signin