import React, { useState, useEffect, useCallback } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link, useNavigate } from 'react-router-dom';
import clsx from 'clsx';
import { makeStyles } from '@material-ui/core/styles';
import Popover from '@material-ui/core/Popover';
import Button from '@material-ui/core/Button';
import Drawer from '@material-ui/core/Drawer';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
import IconButton from '@material-ui/core/IconButton';
import Badge from '@material-ui/core/Badge';
import MenuIcon from '@material-ui/icons/Menu';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import ArrowBackIcon from '@material-ui/icons/ArrowBack';
import NotificationsIcon from '@material-ui/icons/Notifications';
import LogoutIcon from '@material-ui/icons/PowerSettingsNewOutlined';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import Tooltip from '@material-ui/core/Tooltip';
import Fade from '@material-ui/core/Fade';
import logo from '../img/logo.png';
import { logout } from "../actions/auth";
import { clearMessage } from "../actions/message";
import { history } from "../helpers/history";
import EventBus from "../common/EventBus";
import SideMenu from "./SideMenu";

import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import ListSubheader from '@material-ui/core/ListSubheader';
import DashboardIcon from '@material-ui/icons/Dashboard';
import PeopleIcon from '@material-ui/icons/People';
import RepeatIcon from '@material-ui/icons/Repeat';
import GroupAddIcon from '@material-ui/icons/GroupAdd';
import LocalHospitalIcon from '@material-ui/icons/LocalHospital';
import PersonIcon from '@material-ui/icons/Person';
import AssignmentIcon from '@material-ui/icons/Assignment';
import PanToolIcon from '@material-ui/icons/PanTool';
import AutorenewIcon from '@material-ui/icons/Autorenew';
import PlaylistAddCheckIcon from '@material-ui/icons/PlaylistAddCheck';
import HelpOutlineIcon from '@material-ui/icons/HelpOutline';
import InfoIcon from '@material-ui/icons/Info';
import InfoOutlinedIcon from '@material-ui/icons/InfoOutlined';
import profileService from "../services/profile.service";

const drawerWidth = 240;

const useStyles = makeStyles((theme) => ({
    root: {
        display: 'flex',
    }, toolbar: {
        paddingRight: 24, // keep right padding when drawer closed
    }, toolbarIcon: {
        display: 'flex', alignItems: 'center', justifyContent: 'flex-end', padding: '0 8px', ...theme.mixins.toolbar,
    }, appBar: {
        zIndex: theme.zIndex.drawer + 1, transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp, duration: theme.transitions.duration.leavingScreen,
        }),
    }, appBarShift: {
        marginLeft: drawerWidth,
        width: `calc(100% - ${drawerWidth}px)`,
        transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp, duration: theme.transitions.duration.enteringScreen,
        }),
    }, menuButton: {
        marginRight: 36,
    }, menuButtonHidden: {
        display: 'none',
    }, title: {
        flexGrow: 1,
    }, drawerPaper: {
        position: 'relative', whiteSpace: 'nowrap', width: drawerWidth, transition: theme.transitions.create('width', {
            easing: theme.transitions.easing.sharp, duration: theme.transitions.duration.enteringScreen,
        }),
    }, drawerPaperClose: {
        overflowX: 'hidden', transition: theme.transitions.create('width', {
            easing: theme.transitions.easing.sharp, duration: theme.transitions.duration.leavingScreen,
        }), width: theme.spacing(7), [theme.breakpoints.up('sm')]: {
            width: theme.spacing(9),
        },
    }, appBarSpacer: theme.mixins.toolbar, content: {
        flexGrow: 1, height: '100vh', overflow: 'auto',
    }, container: {
        paddingTop: theme.spacing(4), paddingBottom: theme.spacing(4),
    }, paper: {
        padding: theme.spacing(2), display: 'flex', overflow: 'auto', flexDirection: 'column',
    }, fixedHeight: {
        height: 240,
    },
}));


const HeaderAuthenticated = () => {
    const classes = useStyles();
    const navigate = useNavigate();
    const [showModeratorBoard, setShowModeratorBoard] = useState(false);
    const [showAdminBoard, setShowAdminBoard] = useState(false);
    const [anchorEl, setAnchorEl] = React.useState(null);
    const [open, setOpen] = React.useState(true);
    const [profilePhoto, setProfilePhoto] = useState([]);
    const [isProfileNull, setIsProfileNull] = useState(false);
    const [userInfo, setUserInfo] = useState([]);
    const { user: currentUser } = useSelector((state) => state.auth);
    let userId = currentUser.userId;
    // let userId = "2";
    useEffect(() => {
        getProfilePicture();
        getProfileInfo();
    }, []);

    const getProfilePicture = () => {
        profileService.getProfilePicture(userId).then(
            response => {
                setProfilePhoto(response.data.profilePhoto);
                setIsProfileNull(false);
            },
            error => {
                setIsProfileNull(true);
                console.log(
                    (error.response && error.response.data && error.response.data.message) ||
                    error.message || error.toString()
                );
            }
        );
    };

    const getProfileInfo = () => {
        profileService.getProfileInfo(userId).then(
            response => {
                setUserInfo(response.data);
            },
            error => {
                console.log(
                    (error.response && error.response.data && error.response.data.message) ||
                    error.message || error.toString()
                );
            }
        );
    };


    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    const openPopover = Boolean(anchorEl);
    const popOverId = openPopover ? 'simple-popover' : undefined;

    const handleDrawerOpen = () => {
        setOpen(true);
    };
    const handleDrawerClose = () => {
        setOpen(false);
    };


    const dispatch = useDispatch();

    useEffect(() => {
        history.listen((location) => {
            dispatch(clearMessage()); // clear message when changing location
        });
    }, [dispatch]);

    const logOut = useCallback(() => {
        dispatch(logout());
        navigate('/signin');
    }, [dispatch]);

    useEffect(() => {
        if (currentUser) {
            setShowModeratorBoard(currentUser.roles.includes("ROLE_MODERATOR"));
            setShowAdminBoard(currentUser.roles.includes("ROLE_ADMIN"));
        } else {
            setShowModeratorBoard(false);
            setShowAdminBoard(false);
        }

        EventBus.on("logout", () => {
            logOut();
        });

        return () => {
            EventBus.remove("logout");
        };
    }, [currentUser, logOut]);

    return (<div><AppBar position="absolute" className={clsx(classes.appBar, open && classes.appBarShift)}>
        <Toolbar className={classes.toolbar}>
            <IconButton
                edge="start"
                color="inherit"
                aria-label="open drawer"
                onClick={handleDrawerOpen}
                className={clsx(classes.menuButton, open && classes.menuButtonHidden)}
            >
                <MenuIcon />
            </IconButton>
            <Typography component="h1" variant="h6" color="inherit" noWrap className={classes.title}>
                Gyalsung རྒྱལ་སྲུང་།
            </Typography>

            {/* <Typography>
                {currentUser.fullName}
            </Typography> */}

            {/* <IconButton color="inherit">  
              <Badge badgeContent={4} color="secondary">
                    <NotificationsIcon />
                </Badge>
            </IconButton> */}

            <IconButton color="inherit" aria-describedby={popOverId} onClick={handleClick}>
                {/* <img
                    // src={`https://www.citizenservices.gov.bt/BtImgWS/ImageServlet?cidNo=${currentUser.cid}&type=PH`}
                    src="//ssl.gstatic.com/accounts/ui/avatar_2x.png"
                    alt="profile-img" className="profile-img-card-header"
                /> */}

                {isProfileNull == true ?
                    <img src="//ssl.gstatic.com/accounts/ui/avatar_2x.png" className="profile-img-card-header" alt="IMG" />
                    : <img src={`data:image/jpeg;base64,${profilePhoto}`} className="profile-img-card-header" alt="IMG" />
                }
            </IconButton>

            <Typography>
                {/* {currentUser.fullName} */}
                {userInfo.fullName}
            </Typography>
            <Popover
                id={popOverId}
                open={openPopover}
                anchorEl={anchorEl}
                onClose={handleClose}
                anchorOrigin={{
                    vertical: 'bottom',
                    horizontal: 'center',
                }}
                transformOrigin={{
                    vertical: 'top',
                    horizontal: 'center',
                }}
            >
                <div
                    style={{
                        width: '250px',
                        backgroundImage: "linear-gradient(to bottom, #fffb00, rgb(245 167 15))",
                        color: "darkred",
                    }}
                >
                    <div className="d-flex flex-wrap flex-column align-items-center justify-content-center text-muted bg-gray">
                        <div className="mb-2"></div>
                        <div className="mb-2">
                            <span> {userInfo.fullName}</span>
                        </div>
                        <div className="mb-2">
                            {/* <h5> {currentUser.fullName}</h5>
                            {currentUser.mobileNo} */}

                            <span>{userInfo.mobileNo}</span>
                        </div>
                    </div>
                    <Divider />
                    <ListItem button onClick={() => { navigate("/gyalsupProfile"); setAnchorEl(null); }} >
                        <ListItemText primary="Profile" />
                        <ListItemIcon>
                            <PersonIcon />
                        </ListItemIcon>
                    </ListItem>
                    <ListItem button onClick={() => { navigate("/helpCenter"); setAnchorEl(null); }} >
                        <ListItemText primary="Help center" />
                        <ListItemIcon>
                            <HelpOutlineIcon />
                        </ListItemIcon>
                    </ListItem>
                    <ListItem button onClick={() => { navigate("/medicalBooking"); setAnchorEl(null); }} >
                        <ListItemText primary="About" />
                        <ListItemIcon>
                            <InfoOutlinedIcon />
                        </ListItemIcon>
                    </ListItem>
                    <ListItem button onClick={logOut}>
                        <ListItemText primary="Logout" />
                        <ListItemIcon>
                            <ExitToAppIcon />
                        </ListItemIcon>
                    </ListItem>
                    <Divider />
                </div>
            </Popover>

            <Tooltip arrow title="Logout" TransitionComponent={Fade} TransitionProps={{ timeout: 1000 }}>
                <IconButton color="inherit" onClick={logOut}>
                    <ExitToAppIcon />
                </IconButton>
            </Tooltip>
        </Toolbar>
    </AppBar>
        <Drawer
            variant="permanent"
            classes={{
                paper: clsx(classes.drawerPaper, !open && classes.drawerPaperClose),
            }}
            open={open}
        >
            <div className={classes.toolbarIcon}>
                <img src={logo} alt="logo" className="avater" style={{ width: 60, height: 60 }} />
                {/* <h5 className='text-muted'>Gyalsung</h5> */}
                <Typography component="h1" variant="h6" color="inherit" noWrap style={{ color: '#6c757d' }}>
                    Gyalsung
                </Typography>
                <IconButton onClick={handleDrawerClose}>
                    {/* <ChevronLeftIcon /> */}
                    <MenuIcon />
                </IconButton>
            </div>
            <Divider />
            <SideMenu />
        </Drawer>
    </div>)
}

export default HeaderAuthenticated;