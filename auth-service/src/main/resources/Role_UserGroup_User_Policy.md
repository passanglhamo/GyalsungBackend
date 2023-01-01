==================================================================================
Role/UserGroup-User-Policy: Definition:
==================================================================================
User:

    - An user can have many Roles.
    - Or can be assign one or more policies directly with it. [by creating an implicit role with those policies and then attach the role to the new_user]
==================================================================================
Role/UserGroup:

    - A role can have many Users.
    - A role can have many policies.

==================================================================================
Policy Example:

    - A policy can be assigned to many roles.
    - A Policy/Privilage/Permission Consist Of:
        - version_code: version number
        - service_name: Name_Of_The_Service [e.g. name of the micro_service_identifier ]
        - type: managed, created etc
        - statements: Array(of statement)[
                -statement: {
                    - action/privilage: Allow, Block, Read, Write, Delete, Edit etc 
                    - list of actions: Array(of actions)
                    - list of resources: Array(of resources paths) [e.g. rest api paths]
                    ~~- list of condition: Array(of connections)
                }
            ]

Example of aws policy:

    {
        "Version": "2012-10-17",
        "Statement": [
            {
                "Action": [
                    "rds:*",
                    "application-autoscaling:DeleteScalingPolicy",
                    "application-autoscaling:DeregisterScalableTarget",
                    "application-autoscaling:DescribeScalableTargets",
                    "logs:DescribeLogStreams",
                    "logs:GetLogEvents",
                    "outposts:GetOutpostInstanceTypes"
                ],
                "Effect": "Allow",
                "Resource": "*"
            },
            {
                "Action": "pi:*",
                "Effect": "Allow",
                "Resource": "arn:aws:pi:*:*:metrics/rds/*"
            },
            {
                "Action": "iam:CreateServiceLinkedRole",
                "Effect": "Allow",
                "Resource": "*",
                "Condition": {
                    "StringLike": {
                        "iam:AWSServiceName": [
                            "rds.amazonaws.com",
                            "rds.application-autoscaling.amazonaws.com"
                        ]
                    }
                }
            }
        ]
    }
=================================================================================