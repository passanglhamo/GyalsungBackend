import React, {useEffect, useState} from 'react';
import {Button, Dialog, DialogActions, DialogContent, DialogTitle, makeStyles, MenuItem} from '@material-ui/core';
import {DataGrid, GridToolbar} from "@mui/x-data-grid";
import Slide from '@material-ui/core/Slide';
import * as Yup from "yup";
import {Formik} from "formik";
import Select from "../FormsUI/Select";
import FormButton from "../FormsUI/Button";
import activationStatus from "../../data/activationStatus.json";
import trainingAcademyIntakeService from "../../services/trainingAcademyIntake.service";
import trainingAcademyService from "../../services/trainingAcademy.service";
import isEmpty from "validator/es/lib/isEmpty";
import years from "../../data/years.json";
import Number from "../FormsUI/Number";
import {Snackbar, Alert} from '@mui/material';
import TextField from "../FormsUI/Textfield";

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
const TrainingAcademyIntake = () => {

    /*================ constants value =============*/
    const classes = useStyles();
    const [open, setOpen] = useState(false);
    const [trainingAcademyIntakeList, setTrainingAcademyIntakeList] = useState([]);
    const [content, setContent] = useState('');
    const [openSuccess, setOpenSuccess] = useState(false);
    const [openSuccessUpdate, setOpenSuccessUpdate] = useState(false);
    const [trainingAcademyList, setTrainingAcademyList] = useState([]);
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
        academyId: '',
        trainingYear: '',
        maleCapacityAmount: 0,
        femaleCapacityAmount: 0,
        status: defaultStatus

    };

    /*================ form validation =============*/
    const FORM_VALIDATION = Yup.object().shape({
        academyId: Yup.string()
            .required('Training Academy is required'),
        trainingYear: Yup.string()
            .required('Year is required'),
        maleCapacityAmount: Yup.number()
            .required('In-take(Male) is required')
            .typeError('Please enter number only')
            .test(
                'Is positive?',
                'ERROR: The number must be greater than 0!',
                (value) => value > 0
            ),
        femaleCapacityAmount: Yup.number()
            .required('In-take(Female) is required')
            .typeError('Please enter number only')
            .test(
                'Is positive?',
                'ERROR: The number must be greater than 0!',
                (value) => value > 0
            ),
        status: Yup.string()
            .required('Status is required')
        ,
    });

    /*================ Get All method =============*/
    const getTrainingAcademyList = () => {
        trainingAcademyService.getAll().then(
            response => {
                setTrainingAcademyList(response.data._embedded.trainingAcademies)
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
    /*================ Get All method =============*/
    const getAll = () => {
        trainingAcademyIntakeService.getAll().then(
            response => {
                setTrainingAcademyIntakeList(response.data)
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
        getTrainingAcademyList();
        getAll();

    }, [])

    /*================ Submit method =============*/
    const submitForm = (fields, {setSubmitting,resetForm}) => {
        if (isEmpty(id.toString())) {
            create(fields, setSubmitting,resetForm);
        } else {
            update(id, fields, setSubmitting,resetForm);
        }
    }

    /*================ Create method =============*/
    function create(fields, setSubmitting,resetForm) {
        trainingAcademyIntakeService.save(JSON.stringify(fields))
            .then(() => {
                resetPage(resetForm);
            })
            .catch(() => {
                setSubmitting(false);
            });
    }

    /*================ Update method =============*/
    function update(id, fields, setSubmitting,resetForm) {
        trainingAcademyIntakeService.update(id, fields)
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
            width: 70,
            disableClickEventBubbling: true,
            renderCell: (index) => index.api.getRowIndex(index.row.id) + 1,
            sortable: false,
            filterable: false,
        },
        {
            field: 'trainingYear',
            headerName: 'Year',
            width: 120,
            disableClickEventBubbling: true,
        },
        {
            field: 'academyId',
            headerName: 'Training Academy',
            width: 300,
            disableClickEventBubbling: true,
            renderCell: (cellVal) => {

                const acaVal=trainingAcademyList.find(aca=>aca._links.self.href.split("/").pop()==cellVal.value);

                return (
                    <div>
                        {acaVal.name}
                    </div>
                )
            }

        },
        {
            field: 'maleCapacityAmount',
            headerName: 'In take(Male)',
            width: 120,
            disableClickEventBubbling: true,
        },
        {
            field: 'femaleCapacityAmount',
            headerName: 'In take(Female)',
            width: 120,
            disableClickEventBubbling: true,
        },
        {
            field: 'status',
            headerName: 'Status',
            width: 120,
            disableClickEventBubbling: true,
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
            {({values,setFieldValue}) => {
                // eslint-disable-next-line react-hooks/rules-of-hooks
                const [trainingAcademyIntake, setTrainingAcademyIntake] = useState({});
                // eslint-disable-next-line react-hooks/rules-of-hooks
                useEffect(() => {
                    if (!isEmpty(id.toString())) {
                        // get user and set form fields
                        trainingAcademyIntakeService.getAllById(id).then(fieldVal => {
                            const fields = ['trainingYear', 'academyId', 'maleCapacityAmount', 'femaleCapacityAmount', 'status'];
                            fields.forEach(field => setFieldValue(field, fieldVal.data[field], false));
                            setTrainingAcademyIntake(fieldVal.data);
                        });
                    }
                }, [id]);
                return (
                    <div>
                        <div className='col-md-12 row'>
                            <div className="d-flex justify-content-between align-items-center">
                                <strong className="text-muted text-sm-left mb-0 mb-sm-3">
                                    Training Academy in-take</strong>
                                <Button variant="contained" size="small" color="primary"
                                        onClick={() => {
                                            setOpen(true)
                                            setOpenSuccess(false)

                                        }}>
                                    Add new
                                </Button>
                            </div>
                        </div>
                        <div style={{height: 300, width: "100%"}}>
                            <br></br>
                            <DataGrid
                                rows={trainingAcademyIntakeList}
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
                                <DialogTitle id="alert-dialog-slide-title">{"Training Academy in-take"}</DialogTitle>
                                <DialogContent>

                                    <div className="col-md-12 row">
                                        <div className="col-md-12 row">
                                            <Select name="trainingYear" required label="Year" options={years}>
                                            </Select>
                                        </div>
                                        <div className="col-md-12 row">
                                            <TextField select={true} variant='standard' required name="academyId"
                                                       fullWidth={true} label="Training Academy"
                                                       value={values.academyId}
                                                       onChange={item => {
                                                           setFieldValue("academyId", item.target.value);
                                                       }}>
                                                {trainingAcademyList.map((items) => {
                                                    return (
                                                        <MenuItem
                                                            key={items._links.self.href.split("/").pop()}
                                                                  value={items._links.self.href.split("/").pop()}>
                                                            {items.name}
                                                        </MenuItem>
                                                    )
                                                })
                                                }
                                            </TextField>
                                        </div>
                                        <div className="col-md-12 row">
                                            <div className="col-md-6 row">
                                                <Number name="maleCapacityAmount" required label="In-take(Male)"/>
                                            </div>
                                            <div className="col-md-6 row">
                                                <Number name="femaleCapacityAmount" required label="In-take(Female)"/>
                                            </div>
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

export default TrainingAcademyIntake;