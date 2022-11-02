import React, {useEffect, useState, Fragment} from "react";
import {Card, CardBody, Col, FormGroup, Label, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';
import Button from '@material-ui/core/Button';
import {DataGrid} from "@mui/x-data-grid";
import {NativeSelect, Grid} from '@mui/material';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faAdd, faEdit, faLink} from "@fortawesome/free-solid-svg-icons";
import {makeStyles} from "@material-ui/core/styles";
import TextField from "@material-ui/core/TextField";
import InputLabel from '@material-ui/core/InputLabel';
import {ChevronLeft} from "@material-ui/icons";


const useStyle = makeStyles(theme => ({
    root: {
        '& .MuiFormControl-root': {
            width: '80%',
            margin: theme.spacing(1)
        }
    }
}))
const UserList = () => {
    const [showModal, setShowModal] = useState(false);
    const classes = useStyle();

    const [name, setName] = useState(undefined);
    const [email, setEmail] = useState(undefined);
    const [role, setRole] = useState(undefined);
    const [mobileNumber, setMobileNumber] = useState(undefined);



    const toggleModal = () => {
        if (!showModal) {
            setShowModal(true);
        } else {
            setShowModal(false);
        }
    };

    const rows = [
        {id: 1, col1: "1", col2: "Pema Choki", col3: "pema345@gmail.com", col4: "Female", col5: "17471473",col6: "Admin"},
        {id: 2, col1: "2", col2: "Tashi Dorji", col3: "t145@gmail.com", col4: "Male", col5: "17778880",col6: "Operator"},
        {id: 3, col1: "3", col2: "Sonam Tenzin", col3: "sonam@gmail.com", col4: "Male", col5: "17908998",col6: "Operator"},
    ];

    const columns = [
        {
            field: 'col1',
            headerName: 'SL No',
            width: 60,
            disableClickEventBubbling: true,
        },
        {
            field: 'col2',
            headerName: 'Name',
            width: 150,
            disableClickEventBubbling: true,
        },
        {
            field: 'col3',
            headerName: 'Email',
            width: 200,
            disableClickEventBubbling: true,
        },
        {
            field: 'col4',
            headerName: 'Gender',
            width: 190,
            disableClickEventBubbling: true,
        },
        {
            field: 'col5',
            headerName: 'Mobile Number',
            width: 130,
            disableClickEventBubbling: true,
        },
        {
            field: 'col6',
            headerName: 'Role',
            width: 100,
            disableClickEventBubbling: true,
        },
        {
            field: 'col7',
            headerName: '',
            width: 70,
            renderCell: () => {
                return (
                <Button variant="contained" color="primary"
                        onClick={toggleModal}
                > Edit </Button>
                )
            }
        },
    ]

    return (
        <>

            <div className="col-md-12 row">
                <div className="d-flex justify-content-between align-items-center">
                    <strong className="text-muted text-sm-left mb-0 mb-sm-3"> User </strong>
                    <Button variant="contained" color="primary"
                            onClick={toggleModal}
                    > Add new </Button>
                </div>
            </div>

<br/>
            <div className="col-md-12 row">
            <div style={{height: 300, width: "100%"}}>
                <DataGrid
                    rows={rows}
                    columns={columns}
                    pageSize={5}
                    rowsPerPageOptions={[5]}
                />
            </div>
            </div>

            <Modal isOpen={showModal} centered size="lg">
                <ModalHeader>User Information</ModalHeader>
                <ModalBody>
                    <form className={classes.root}>
                        <Grid container>
                            <Grid item xs={4}>
                                <TextField id="reason"
                                           multiline
                                           label="Name"
                                           variant="standard"
                                           value={name}
                                           onChange={e => setName(e.target.value)}/>
                            </Grid>
                            <Grid item xs={4}>
                                <TextField id="email"
                                           multiline
                                           label="Email"
                                           variant="standard"
                                           value={email}
                                           onChange={e => setEmail(e.target.value)}/>
                            </Grid>
                            <Grid item xs={4} >
                                <TextField id="mobileNumber"
                                           multiline
                                           label="Mobile Number"
                                           variant="standard"
                                           value={mobileNumber}
                                           onChange={e => setMobileNumber(e.target.value)}/>
                            </Grid>
                            <Grid item xs={4} padding={1}>
                                <InputLabel variant="standard" htmlFor="uncontrolled-native">
                                    Gender
                                </InputLabel>
                                <NativeSelect
                                    defaultValue={30}
                                    inputProps={{
                                        name: 'role',
                                        id: 'uncontrolled-native',
                                    }}
                                >
                                    <option value={1}>Male</option>
                                    <option value={2}>Female</option>
                                    <option value={3}>Other</option>
                                </NativeSelect>
                            </Grid>
                            <Grid item xs={4} padding={1} >
                                <InputLabel variant="standard" htmlFor="uncontrolled-native">
                                    Role
                                </InputLabel>
                                <NativeSelect
                                    defaultValue={30}
                                    inputProps={{
                                        name: 'role',
                                        id: 'uncontrolled-native',
                                    }}
                                >
                                    <option value={1}>Admin</option>
                                    <option value={2}>Operator</option>
                                    <option value={3}>Doctor</option>
                                </NativeSelect>
                            </Grid>

                        </Grid>
                    </form>
                </ModalBody>
                <ModalFooter>
                    <Button color="secondary" variant="contained" onClick={toggleModal}>
                        Close
                    </Button>
                    <Button color="primary" variant="contained">
                        Add
                    </Button>
                </ModalFooter>
            </Modal>
        </>
    )

}

export default UserList