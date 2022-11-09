import './App.css';
import './animate.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import React from "react";
import { useSelector } from "react-redux";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { makeStyles } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import Container from '@material-ui/core/Container';
import Signup from './components/Signup';
import ScreenDashboard from './components/ScreenDashboard';
import GeneralDashboard from './components/GeneralDashboard';
import Signin from './components/Signin';
import ForgotPassword from './components/ForgotPassword';
import GyalsupProfile from './components/userProfile/GyalsupProfile';
import Enrolment from './components/enrolment/Enrolment';
import ApplyDeferment from './components/deferment/ApplyDeferment';
import MedicalBooking from './components/medicalBooking/MedicalBooking';
import EditMedicalBooking from './components/medicalBooking/EditMedicalBooking';
import ApplyExemption from './components/Exemption/ApplyExemption';
import RegisteredUser from './components/RegisteredUser';

import { history } from "./helpers/history";
import TestTable from './components/TestTable';
import HeaderPublic from './components/HeaderPublic';
import HeaderAuthenticated from './components/HeaderAuthenticated';
import { Dashboard } from '@material-ui/icons';
import ResetPassword from './components/ResetPassword';
import DefermentList from "./components/deferment/DefermentList";
import ExemptionList from "./components/Exemption/ExemptionList";
import EnrolmentList from './components/EnrolmentList';
import AcademyWiseEnrolementList from './components/AcademyWiseEnrolementList';
import TrainingAcademyDashboard from './components/TrainingAcademyDashboard';
import UserList from "./components/UserList";
import FieldSpecialization from './components/master/FieldSpecialization';
import EnlistmentDateSetup from './components/master/EnlistmentDateSetup';
import MedicalQuestionnaire from './components/master/MedicalQuestionnaire';
import ResetUserPassword from "./components/ResetUserPassword";
import RoleCreation from './components/RoleCreation';
import UserPermission from './components/UserPermission';
import TrainingAcademyIntake from "./components/master/TrainingAcademyIntake";
import NoticeConfiguration from "./components/master/NoticeConfiguration";
import DzongkhagHospitalMapping from "./components/master/DzongkhagHospitalMapping";
import Reason from "./components/master/Reason";
import EditMobileNo from './components/userProfile/EditMobileNo';
import EditEmailAddress from './components/userProfile/EditEmailAddress';
import EditCurrentAddress from './components/userProfile/EditCurrentAddress';
import EditSocialMediaLink from './components/userProfile/EditSocialMediaLink';
import EditParentInfo from './components/userProfile/EditParentInfo';
import EditGuardianInfo from './components/userProfile/EditGuardianInfo';
import EditPasswordChange from './components/userProfile/EditPasswordChange';
import HospitalScheduleTime from "./components/master/HospitalScheduleTime";
import HospitalScheduleTimeList from './components/master/HospitalScheduleTimeList';
import EditSyncCensusRecord from './components/userProfile/EditSyncCensusRecord';
import EditUsername from './components/userProfile/EditUsername';
import MedicalCategory from "./components/master/MedicalCategorySetup";
import SubmitParentConsent from './components/enrolment/SubmitParentConsent';
import ParentConsentList from './components/enrolment/ParentConsentList';
import { ColorModeContext, tokens, useMode } from "./theme";
import PreviousDeclaration from './components/medicalBooking/PreviousDeclaration';
import ResubmitSelfDeclaration from './components/medicalBooking/ResubmitSelfDeclaration';

const drawerWidth = 240;

const useStyles = makeStyles((theme) => ({
    root: {
        display: 'flex',
    },
    toolbar: {
        paddingRight: 24, // keep right padding when drawer closed
    },
    toolbarIcon: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'flex-end',
        padding: '0 8px',
        ...theme.mixins.toolbar,
    },
    appBar: {
        zIndex: theme.zIndex.drawer + 1,
        transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
        }),
    },
    appBarShift: {
        marginLeft: drawerWidth,
        width: `calc(100% - ${drawerWidth}px)`,
        transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    },
    menuButton: {
        marginRight: 36,
    },
    menuButtonHidden: {
        display: 'none',
    },
    title: {
        flexGrow: 1,
    },
    drawerPaper: {
        position: 'relative',
        whiteSpace: 'nowrap',
        width: drawerWidth,
        transition: theme.transitions.create('width', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    },
    drawerPaperClose: {
        overflowX: 'hidden',
        transition: theme.transitions.create('width', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
        }),
        width: theme.spacing(7),
        [theme.breakpoints.up('sm')]: {
            width: theme.spacing(9),
        },
    },
    appBarSpacer: theme.mixins.toolbar,
    content: {
        flexGrow: 1,
        height: '100vh',
        // overflow: 'auto',
    },
    container: {
        paddingTop: theme.spacing(4),
        paddingBottom: theme.spacing(4),
    },
    paper: {
        padding: theme.spacing(2),
        display: 'flex',
        overflow: 'auto',
        flexDirection: 'column',
    },
    fixedHeight: {
        height: 240,
    },
}));

const App = () => {
    const classes = useStyles();
    const { user: currentUser } = useSelector((state) => state.auth);
    const [theme, colorMode] = useMode();
    const colors = tokens(theme.palette.mode);
    return (
        <ColorModeContext.Provider value={colorMode}>
            <Router history={history}>
                <div className={classes.root}>
                    <CssBaseline />
                    {!currentUser ? (<HeaderPublic />) : (<HeaderAuthenticated />)}
                    <main className={classes.content}>
                        <div className={classes.appBarSpacer} />
                        <Container maxWidth="lg" className={classes.container}>
                            <Routes>
                                <Route exact path="/" element={<GeneralDashboard />}></Route>
                                <Route path="/signup" element={<Signup />}></Route>
                                <Route path="/signin" element={<Signin />}></Route>
                                <Route path="/forgotPassword" element={<ForgotPassword />}></Route>
                                <Route path="/resetPassword" element={<ResetPassword />}></Route>
                                <Route path="/enrolment" element={<Enrolment />}></Route>
                                <Route path="/enrolmentList" element={<EnrolmentList />}></Route>
                                <Route path="/applyDeferment" element={<ApplyDeferment />}></Route>
                                <Route path="/defermentList" element={<DefermentList />}></Route>
                                <Route path="/applyExemption" element={<ApplyExemption />}></Route>
                                <Route path="/exemptionList" element={<ExemptionList />}></Route>
                                <Route path="/medicalBooking" element={<MedicalBooking />}></Route>
                                <Route path="/editMedicalBooking" element={<EditMedicalBooking />}></Route>
                                <Route path="/previousDeclaration" element={<PreviousDeclaration />}></Route>
                                <Route path="/resubmitSelfDeclaration" element={<ResubmitSelfDeclaration />}></Route>
                                <Route path="/gyalsupProfile" element={<GyalsupProfile />}></Route>
                                <Route path="/userList" element={<UserList />}></Route>
                                <Route path="/roleCreation" element={<RoleCreation />}></Route>
                                <Route path="/userPermission" element={<UserPermission />}></Route>
                                <Route path="/fieldSpecialization" element={<FieldSpecialization />}></Route>
                                <Route path="/academyWiseEnrolment" element={<AcademyWiseEnrolementList />}></Route>
                                <Route path="/enlistmentDate" element={<EnlistmentDateSetup />}></Route>
                                <Route path="/medicalCategory" element={<MedicalCategory />}></Route>
                                <Route path="/medicalQuestionnaire" element={<MedicalQuestionnaire />}></Route>
                                <Route path="/resetUserPassword" element={<ResetUserPassword />}></Route>
                                <Route path="/trainingAcademyIntake" element={<TrainingAcademyIntake />}></Route>
                                <Route path="/noticeConfiguration" element={<NoticeConfiguration />}></Route>
                                <Route path="/dzongkhagHospitalMapping" element={<DzongkhagHospitalMapping />}></Route>
                                <Route path="/hospitalScheduleTime" element={<HospitalScheduleTime />}></Route>
                                <Route path="/hospitalScheduleTimeList" element={<HospitalScheduleTimeList />}></Route>
                                <Route path="/reason" element={<Reason />}></Route>
                                <Route path="/registeredUser" element={<RegisteredUser />}></Route>
                                <Route path="/trainingAcademyDashboard" element={<TrainingAcademyDashboard />}></Route>
                                <Route path="/editMobileNo" element={<EditMobileNo />}></Route>
                                <Route path="/editEmailAddress" element={<EditEmailAddress />}></Route>
                                <Route path="/editCurrentAddress" element={<EditCurrentAddress />}></Route>
                                <Route path="/editSocialMediaLink" element={<EditSocialMediaLink />}></Route>
                                <Route path="/editParentInfo" element={<EditParentInfo />}></Route>
                                <Route path="/editGuardianInfo" element={<EditGuardianInfo />}></Route>
                                <Route path="/editPasswordChange" element={<EditPasswordChange />}></Route>
                                <Route path="/editSynceCensusRecord" element={<EditSyncCensusRecord />}></Route>
                                <Route path="/submitParentConsent" element={<SubmitParentConsent />}></Route>
                                <Route path="/parentConsentList" element={<ParentConsentList />}></Route>
                                <Route path="/editUsername" element={<EditUsername />}></Route>
                                <Route path="/screenDashboard" element={<ScreenDashboard />}></Route>
                                <Route path="/testForm" element={<TestTable />}></Route>
                            </Routes>
                        </Container>
                    </main>
                </div>
            </Router>
        </ColorModeContext.Provider>
    );
}

export default App;
