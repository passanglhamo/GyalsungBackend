import PageHeader from "./PageHeader";
import React, { useEffect, useState } from "react";
import Button from '@material-ui/core/Button';
import { DataGrid } from "@mui/x-data-grid";
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormHelperText from '@material-ui/core/FormHelperText';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import { makeStyles } from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle'
import Slide from '@material-ui/core/Slide';
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
const AcademyWiseEnrolementList = () => {
    const classes = useStyles();
    const [tableData, setTableData] = useState([])
    const [content, setContent] = useState('');
    const [open, setOpen] = useState(false);


    const handleClose = () => {
        setOpen(false);
    };
    const rows = [
        { id: 1, col1: "1", col2: "Passang Lhamo", col3: "11510200452", col4: "December 10, 1982", col5: "Female" },
        { id: 2, col1: "2", col2: "Ngawang Zepa", col3: "11514000454", col4: "March 15, 1994", col5: "Male" },
        { id: 3, col1: "3", col2: "Namgay Wangchuk", col3: "10520104456", col4: "July 07, 2000", col5: "Male" },
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
            width: 200,
        },
        {
            field: 'col3',
            headerName: 'CID',
            width: 200,
            disableClickEventBubbling: true,
        },
        {
            field: 'col4',
            headerName: 'Date of birth',
            width: 200,
            disableClickEventBubbling: true,
        },
        {
            field: 'col5',
            headerName: 'Gender',
            width: 100,
            disableClickEventBubbling: true,
        }
    ]

    return (<div>
        <div className='col-md-12 row'>
            <div className='col-md-12 row'>
                <div className='col-md-4'>
                    <FormControl className={classes.formControl}>
                        <InputLabel id="demo-simple-select-helper-label">Year</InputLabel>
                        <Select
                            labelId="demo-simple-select-helper-label"
                            id="demo-simple-select-helper"
                        >
                            <MenuItem value={1}><span>2023</span></MenuItem>
                            <MenuItem value={2}><span>2024</span></MenuItem>
                            <MenuItem value={3}><span>2025</span></MenuItem>
                            <MenuItem value={4}><span>2026</span></MenuItem>
                            <MenuItem value={5}><span>2027</span></MenuItem>
                        </Select>
                    </FormControl>
                </div>
                <div className='col-md-4'>
                    <FormControl className={classes.formControl}>
                        <InputLabel id="demo-simple-select-helper-label">Academy</InputLabel>
                        <Select
                            labelId="demo-simple-select-helper-label"
                            id="demo-simple-select-helper"
                        >
                            <MenuItem value={1}><span>Gyalposhing(Mongar)</span></MenuItem>
                            <MenuItem value={2}><span>Taraythang(Gelephu)</span></MenuItem>
                            <MenuItem value={3}><span>Khotakha(Wangduephodrang)</span></MenuItem>
                            <MenuItem value={4}><span>Phuntshothang(Samdrupjonkhar)</span></MenuItem>
                            <MenuItem value={5}><span>Jamtsholing(Samtse)</span></MenuItem>
                        </Select>
                    </FormControl>
                </div>
                <div className='col-md-4'>
                    <Button variant="contained" color="primary"
                    >   Search </Button>
                </div>
            </div>
        </div>

        <Button variant="contained" color="primary"
            onClick={() => setOpen(true)}>  Change training institute </Button>


        <div style={{ height: 300, width: "100%" }}>
            <br></br>
            <DataGrid
                rows={rows}
                columns={columns}
                pageSize={5}
                rowsPerPageOptions={[5]}
                checkboxSelection
            />
        </div>
        <Dialog
            open={open}
            TransitionComponent={Transition}
            keepMounted
            onClose={handleClose}
            aria-labelledby="alert-dialog-slide-title"
            aria-describedby="alert-dialog-slide-description"
        >
            <DialogTitle id="alert-dialog-slide-title">{"Change training institute"}</DialogTitle>
            <DialogContent>
                <div className="col-md-12">
                    <FormControl className={classes.formControl}>
                        <InputLabel id="demo-simple-select-helper-label">Change Training Academy</InputLabel>
                        <Select labelId="demo-simple-select-helper-label" id="demo-simple-select-helper">
                            <MenuItem value={1}><span>Gyalposhing(Mongar)</span></MenuItem>
                            <MenuItem value={2}><span>Taraythang(Gelephu)</span></MenuItem>
                            <MenuItem value={3}><span>Khotakha(Wangduephodrang)</span></MenuItem>
                            <MenuItem value={4}><span>Phuntshothang(Samdrupjonkhar)</span></MenuItem>
                            <MenuItem value={5}><span>Jamtsholing(Samtse)</span></MenuItem>
                        </Select>
                        <TextField id="reason" multiline label="Remarks" variant="standard" />
                    </FormControl>
                </div>
            </DialogContent>
            <DialogActions>
                <Button variant="contained" color="secondary"
                    onClick={handleClose} >  Cancel </Button>
                <Button onClick={handleClose} variant="contained" color="primary">
                    Yes, Confirm
                </Button>
            </DialogActions>
        </Dialog>
    </div>
    )
}
export default AcademyWiseEnrolementList