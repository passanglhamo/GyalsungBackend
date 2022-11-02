import React from 'react';
import {TextField} from '@material-ui/core';
import {useField} from 'formik';

const FileUploadWrapper = ({
                               name,
                               ...otherProps
                           }) => {
    const [field, mata] = useField(name);

    const configFileUpload = {
        ...field,
        ...otherProps,
        fullWidth: true,
        variant: 'outlined',
        type: 'file'
}

    if (mata && mata.touched && mata.error) {
        configFileUpload.error = true;
        configFileUpload.helperText = mata.error;
    }

    return (
           <TextField {...configFileUpload} />
    );
};

export default FileUploadWrapper;