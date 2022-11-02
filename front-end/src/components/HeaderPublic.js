import React from "react";
import { useNavigate } from 'react-router-dom';
import clsx from 'clsx';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import logo from '../img/logo.png';
import Button from '@material-ui/core/Button';

const useStyles = makeStyles((theme) => ({
    toolbar: {
        paddingRight: 24, // keep right padding when drawer closed
    },
    appBar: {
        zIndex: theme.zIndex.drawer + 1,
        transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
        }),
    },
    title: {
        flexGrow: 1,
    },
}));

const HeaderPublic = () => {
    const classes = useStyles();
    const navigate = useNavigate();
    return (
        <div>
            <AppBar position="absolute" className={clsx(classes.appBar,)}>
                <Toolbar  >
                    <img src={logo} alt="logo" className="avater" style={{ width: 60, height: 60 }} />

                    <Typography component="h1" variant="h6" color="inherit" noWrap className={classes.title}>
                        Gyalsung རྒྱལ་སྲུང་།
                    </Typography>

                    <div style={{ marginRight: '4px' }}>
                        <Button color="inherit"
                            onClick={() => navigate("/signup")} > Signup </Button>

                    </div>
                    <Button color="inherit" onClick={() => navigate("/signin")}>Login</Button>

                    {/* <Button variant="contained" color="primary"
                        onClick={() => navigate("/signin")} > Log in </Button> */}
                </Toolbar>
            </AppBar>
        </div>
    )
}

export default HeaderPublic;