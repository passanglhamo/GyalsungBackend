import React, { useEffect, useState } from 'react';
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import { makeStyles } from '@material-ui/core/styles';
import { Button, Dialog, DialogActions, DialogContent, DialogTitle } from '@material-ui/core';
import parentConsentService from '../../services/parentConsent.service';

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


const ParentConsentList = () => {
    const [parentConsentList, setParentConsentList] = useState([]);

    useEffect(() => {
        getParentConsentList();
    }, [])

    const getParentConsentList = () => {
        parentConsentService.getParentConsentList().then(
            response => {
                setParentConsentList(response.data)
            },
            error => {
                // setContent(
                //     (error.response &&
                //         error.response.data &&
                //         error.response.data.message) ||
                //     error.message ||
                //     error.toString()
                // );
            }
        );

    }

    const columns = [
        {
            field: 'col1',
            headerName: 'SL No',
            width: 100,
            disableClickEventBubbling: true,
            renderCell: (index) => index.api.getRowIndex(index.row.userId) + 1,
            sortable: false,
            filterable: false,
        },
        {
            field: 'fullName',
            headerName: 'Full Name',
            width: 150,
        },
        {
            field: 'cid',
            headerName: 'CID',
            width: 150,
        },
        {
            field: 'dob',
            headerName: 'DOB',
            width: 150,
        },
        {
            field: 'guardianName',
            headerName: 'Guardian Name',
            width: 150,
        },
        {
            field: 'guardianMobileNo',
            headerName: 'Guardian Mobile No',
            width: 150,
        },

    ]
    return (
        <div>
            <div style={{ height: 300, width: "100%" }}>
                <br></br>
                <DataGrid
                    rows={parentConsentList}
                    columns={columns}
                    pageSize={5}
                    rowsPerPageOptions={[5]}
                    components={{ Toolbar: GridToolbar }}
                    getRowId={(row) => row.userId}
                    autoHeight
                />
            </div>
        </div>
    )
}

export default ParentConsentList