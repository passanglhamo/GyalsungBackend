import React, {useEffect, useState} from 'react';
import Button from '@material-ui/core/Button';
import {DataGrid, GridToolbar} from "@mui/x-data-grid";
import {makeStyles} from '@material-ui/core/styles';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle'
import Slide from '@material-ui/core/Slide';
import TextField from "../FormsUI/Textfield";
import * as Yup from "yup";
import {Formik} from "formik";
import Select from "../FormsUI/Select";
import FormButton from "../FormsUI/Button";
import activationStatus from "../../data/activationStatus.json";
import medicalCategorySetupService from "../../services/medicalCategorySetup.service";
import medicalQuestionnaireService from "../../services/medicalQuestionnaire.service";
import {MenuItem} from "@material-ui/core";
import isEmpty from "validator/es/lib/isEmpty";
import {Snackbar, Alert} from '@mui/material';


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
const Transition = React.forwardRef(function Transition(props, ref) {
    return <Slide direction="up" ref={ref} {...props} />;
});
const MedicalQuestionnaire = () => {

    /*================ constants value =============*/
    const classes = useStyles();
    const [open, setOpen] = useState(false);
    const [medicalCategoryList, setMedicalCategoryList] = useState([]);
    const [medicalQuestionnaireList, setMedicalQuestionnaireList] = useState([]);
    const [content, setContent] = useState('');
    const [openSuccess, setOpenSuccess] = useState(false);
    const [openSuccessUpdate, setOpenSuccessUpdate] = useState(false);
    const [id, setId] = useState("");
    const [defaultStatus, setDefaultStatus] = useState('A');

    const handleClose = () => {
        setOpen(false);
    };
    const handleCloseSuccess = () => {
        setOpenSuccess(false);
    };

    const handleCloseSuccessUpdate = () => {
        setOpenSuccessUpdate(false);
    };

    const resetPage = (resetForm) => {
        setId("");
        resetForm();
        getAll();
        handleClose();
        setOpenSuccess(true);

    };
    const resetPageUpdate = (resetForm) => {
        setId("");
        resetForm();
        getAll();
        handleClose();
        setOpenSuccessUpdate(true);

    };

    /*================ Initial value for the fields =============*/
    const INITIAL_FORM_STATE = {
        categoryId: '',
        medicalQuestionName: '',
        status:defaultStatus
    };

    /*================ form validation =============*/
    const FORM_VALIDATION = Yup.object().shape({
        categoryId: Yup.string()
            .required('Medical Category is required'),
        medicalQuestionName: Yup.string()
            .required('Medical Questionaire is required'),
        status: Yup.string()
            .required('Status is required'),
    });

    /*================ Get All method =============*/
    const getAll = () => {
        medicalQuestionnaireService.getAll().then(
            response => {
                setMedicalQuestionnaireList(response.data)
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

    /*================ use effect =============*/
    useEffect(() => {
        medicalCategorySetupService.getAllActiveList().then(
            response => {
                setMedicalCategoryList(response.data)
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

        getAll();

    }, [])

    /*================ Submit method =============*/
    const submitForm = (fields, { setSubmitting,resetForm }) => {
        if (isEmpty(id.toString())) {
            create(fields, setSubmitting,resetForm);
        } else {
            update(id,fields, setSubmitting,resetForm);
        }
    }

    /*================ Create method =============*/
    function create(fields, setSubmitting,resetForm) {
        medicalQuestionnaireService.save(JSON.stringify(fields))
            .then(() => {
                resetPage(resetForm);
            })
            .catch(() => {
                setSubmitting(false);
            });
    }

    /*================ Update method =============*/
    function update(id, fields, setSubmitting,resetForm) {
        medicalQuestionnaireService.update(id, fields)
            .then(() => {
                resetPageUpdate(resetForm);
            })
            .catch(error => {
                setSubmitting(false);
            });
    }

    /*================Table Columns =============*/
    const columns = [
        {
            field: 'col1',
            headerName: 'SL No',
            width: 100,
            disableClickEventBubbling: true,
            renderCell: (index) => index.api.getRowIndex(index.row.id) + 1,
            sortable: false,
            filterable: false,
        },
        {
            field: 'categoryId',
            headerName: 'Medical category',
            width: 300,
            renderCell: (cellVal) => {

                const catVal=medicalCategoryList.find(med=>med.id==cellVal.value);

                return (
                    <div>
                        {catVal.categoryName}
                    </div>
                )
            }

        },
        {
            field: 'medicalQuestionName',
            headerName: 'Medical Question',
            width: 400,
        },
        {
            field: 'status',
            headerName: 'Status',
            width: 200,
            renderCell: (cellVal) => activationStatus[cellVal.value],
        },
        {
            field: 'col3',
            headerName: 'Action',
            width: 100,
            disableClickEventBubbling: true,
            sortable: false,
            filterable: false,
            renderCell: (cellValue) => {
                return (
                    <Button variant="contained" size="small" color="primary" onClick={() => {
                        setId(cellValue.id);
                        setOpen(true)
                    }}>
                        Edit
                    </Button>
                )
            }
        }
    ]

    return (
        <Formik initialValues={{...INITIAL_FORM_STATE}}
                validationSchema={FORM_VALIDATION}
                onSubmit={submitForm}>
            {({setFieldValue}) => {
                // eslint-disable-next-line react-hooks/rules-of-hooks
                const [medicalQuestionnaire, setMedicalQuestionnaire] = useState({});                // eslint-disable-next-line react-hooks/rules-of-hooks
                useEffect(() => {
                    if (!isEmpty(id.toString())) {
                        medicalQuestionnaireService.getAllById(id).then(fieldVal => {
                            const fields = ['categoryId','medicalQuestionName','status'];
                            fields.forEach(field => setFieldValue(field, fieldVal.data[field], false));
                            setMedicalQuestionnaire(fieldVal.data);
                        });
                    }
                }, [id]);
                return (
                    <div>
                        <div className='col-md-12 row'>
                            <div className="d-flex justify-content-between align-items-center">
                                <strong className="text-muted text-sm-left mb-0 mb-sm-3">
                                    Medical Questionnaire setup</strong>
                                <Button variant="contained" size="small" color="primary"
                                        onClick={() => {
                                            setOpen(true)
                                        }}>
                                    Add new
                                </Button>
                            </div>
                        </div>
                        <div style={{height: 300, width: "100%"}}>
                            <br></br>
                            <DataGrid
                                rows={medicalQuestionnaireList}
                                columns={columns}
                                pageSize={5}
                                rowsPerPageOptions={[5]}
                                components={{Toolbar: GridToolbar}}
                                getRowId={(row) => row.id}
                                autoHeight
                            />
                            <Snackbar open={openSuccess} autoHideDuration={2000} onClose={handleCloseSuccess}
                                      anchorOrigin={{
                                          vertical:'bottom',
                                          horizontal:'right'
                                      }}>
                                <Alert severity="success">Data added successfully.</Alert>
                            </Snackbar>
                            <Snackbar open={openSuccessUpdate} autoHideDuration={2000} onClose={handleCloseSuccessUpdate}
                                      anchorOrigin={{
                                          vertical:'bottom',
                                          horizontal:'right'
                                      }}>
                                <Alert severity="success">Data updated successfully.</Alert>
                            </Snackbar>
                        </div>
                        <Dialog
                            open={open}
                            TransitionComponent={Transition}
                            keepMounted
                            onClose={handleClose}
                            aria-labelledby="alert-dialog-slide-title"
                            aria-describedby="alert-dialog-slide-description"
                        >
                            <form className={classes.root}>
                                <DialogTitle id="alert-dialog-slide-title">{"Medical Category Setup"}</DialogTitle>
                                <DialogContent>

                                    <div className="col-md-12 row">
                                        <div className="col-md-12 row">
                                            <TextField select={true} variant='standard' required name="categoryId"
                                                       fullWidth={true} label="Medical Category">
                                                {medicalCategoryList.map((items) => {
                                                    return (
                                                        <MenuItem key={items.status}
                                                                  value={items.id}>
                                                            {items.categoryName}
                                                        </MenuItem>
                                                    )
                                                })
                                                }
                                            </TextField>
                                        </div>
                                        <div className="col-md-12 row">
                                            <TextField name="medicalQuestionName" required label="Medical Questionnaire"

                                            />
                                        </div>
                                        <div className="col-md-12 row">
                                            <Select name="status" label="Status" required options={activationStatus}>
                                            </Select>
                                        </div>
                                    </div>

                                </DialogContent>
                                <DialogActions>
                                    <Button color="secondary" variant="contained" onClick={handleClose}>
                                        Cancel
                                    </Button>
                                    <FormButton type="submit" buttonName="Submit">
                                    </FormButton>
                                </DialogActions>
                            </form>
                        </Dialog>
                    </div>

                );

            }}
        </Formik>
    )
}

export default MedicalQuestionnaire;