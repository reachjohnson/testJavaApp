var maintenancemodel = require('../models/maintenancemodel.js');
var async = require("async");

module.exports = {
    loadAddUser: function (request, response, next) {
        response.render('maintenance/addUser.ejs', {
            NewUserId: AppUtil.checkNull(request.NewUserId),
            FirstName: request.FirstName,
            LastName: request.LastName,
            AdminFlag: request.AdminFlag,
            ActiveFlag: request.ActiveFlag,
            AssigneeFlag: request.AssigneeFlag,
            WriteAccess: request.WriteAccess,
            DomainName: request.DomainName,
            ContactNumber: request.ContactNumber,
            Result: request.Result
        });
    },
    addUser: function (request, response, next) {
        var UserId = AppUtil.checkNull(request.body.UserId);
        var FirstName = AppUtil.checkNull(request.body.FirstName);
        var LastName = AppUtil.checkNull(request.body.LastName);
        var AdminFlag = request.body.Admin;
        var ActiveFlag = request.body.Active;
        var AssigneeFlag = request.body.IsAssignee;
        var WriteAccess = (request.body.WriteAccess == "on") ? "Y" : "N";
        var DomainName = AppUtil.checkNull(request.body.DomainName);
        var ContactNumber = AppUtil.checkNull(request.body.ContactNumber);
        maintenancemodel.checkUserIdExists(UserId, function (error, UserIdExists) {
            if (error) {
                AppUtil.renderErrorPage(true, response, error);
            }
            else if (UserIdExists) {
                request.NewUserId = UserId;
                request.FirstName = FirstName;
                request.LastName = LastName;
                request.AdminFlag = AdminFlag;
                request.ActiveFlag = ActiveFlag;
                request.AssigneeFlag = AssigneeFlag;
                request.WriteAccess = WriteAccess;
                request.DomainName = DomainName;
                request.ContactNumber = ContactNumber;
                request.Result = "USERIDEXISTS";
                request.url = '/loadAddUser';
                next('route');
            }
            else {
                maintenancemodel.addUser(UserId, FirstName, LastName, AdminFlag, ActiveFlag, AssigneeFlag, WriteAccess, DomainName, ContactNumber, function (error) {
                    if (error) {
                        AppUtil.renderErrorPage(true, response, error);
                    }
                    else {
                        AppUtil.sendEmail([UserId+"@"+DomainName+AppConstants.DOMAIN_SUFFIX], [request.session.userid+"@"+request.session.domainname+AppConstants.DOMAIN_SUFFIX], [], AppConstants.MAILLOGINADDSUBJECT,
                            [FirstName + " " + LastName, AppConstants.MAILLOGINADDSUBJECT], ["User Id", UserId, "Password", AppConstants.DEFAULT_PASSWORD],
                            true, [], request.session.username, function (error) {
                                if (error) {
                                    //AppUtil.renderErrorPage(true, response, error);
                                }
                            });
                        request.Result = AppConstants.SUCCESS;
                        request.url = '/loadAddUser';
                        next('route');
                    }
                });
            }
        });
    },
    loadModifyUser: function (request, response, next) {
        var UserIdRefNo = AppUtil.checkNull(request.body.UserIdRefNo);
        async.parallel([
                function (callback) {
                    maintenancemodel.getUserDetails(UserIdRefNo, function (error, UserDetails) {
                        callback(error, UserDetails);
                    });
                },
                function (callback) {
                    maintenancemodel.getUserList(function (error, UserList) {
                        callback(error, UserList);
                    });
                }
            ],
            function (error, results) {
                if (error) {
                    AppUtil.renderErrorPage(true, response, error);
                }
                else {
                    response.render('maintenance/modifyUser.ejs', {
                        UserDetails: results[0],
                        UserList: results[1],
                        SelectedUserIdRefNo: UserIdRefNo,
                        Result: request.Result
                    });
                }
            });
    },
    modifyUser: function (request, response, next) {
        var UserIdRefNo = request.body.UserIdRefNo;
        var NewUserId = AppUtil.checkNull(request.body.NewUserId);
        var OldUserId = AppUtil.checkNull(request.body.OldUserId);
        var FirstName = AppUtil.checkNull(request.body.FirstName);
        var LastName = AppUtil.checkNull(request.body.LastName);
        var AdminFlag = request.body.Admin;
        var ActiveFlag = request.body.Active;
        var AssigneeFlag = request.body.IsAssignee;
        var ResetPassword = (request.body.ResetPassword == "on") ? "Y" : "N";
        var WriteAccess = (request.body.WriteAccess == "on") ? "Y" : "N";
        var DomainName = AppUtil.checkNull(request.body.DomainName);
        var ContactNumber = AppUtil.checkNull(request.body.ContactNumber);
        maintenancemodel.checkUserIdExistsForUpdate(NewUserId, OldUserId, function (error, UserIdExists) {
            if (error) {
                AppUtil.renderErrorPage(true, response, error);
            }
            else if (UserIdExists) {
                request.Result = "USERIDEXISTS";
                request.url = '/loadModifyUser';
                next('route');
            }
            else {
                maintenancemodel.modifyUser(UserIdRefNo, NewUserId, FirstName, LastName, AdminFlag, ActiveFlag,
                    AssigneeFlag, ResetPassword, WriteAccess, DomainName, ContactNumber, function (error) {
                        if (error) {
                            AppUtil.renderErrorPage(true, response, error);
                        }
                        else {
                            if (ResetPassword == "Y") {
                                AppUtil.sendEmail([NewUserId+"@"+DomainName+AppConstants.DOMAIN_SUFFIX], [request.session.userid+"@"+request.session.domainname+AppConstants.DOMAIN_SUFFIX], [], AppConstants.MAILPASSWORDRESETSUBJECT,
                                    [FirstName + " " + LastName, AppConstants.MAILPASSWORDRESETSUBJECT], ["User Id", NewUserId, "Password", AppConstants.DEFAULT_PASSWORD],
                                    true, [], request.session.username, function (error) {
                                        if (error) {
                                            //AppUtil.renderErrorPage(true, response, error);
                                        }
                                    });
                            }
                            request.Result = AppConstants.SUCCESS;
                            request.url = '/loadModifyUser';
                            next('route');
                        }
                    });
            }
        });
    },

    loadSPOCAndDL: function (request, response, next) {
        var SelectedTeam = AppUtil.checkNull(request.body.Team);
        async.parallel([
                function (callback) {
                    maintenancemodel.getVendorLists(function (error, TeamList) {
                        callback(error, TeamList);
                    });
                },
                function (callback) {
                    maintenancemodel.getSPOCDLDetails(SelectedTeam, function (error, SPOCDLDetails) {
                        callback(error, SPOCDLDetails);
                    });
                }
            ],
            function (error, results) {
                if (error) {
                    AppUtil.renderErrorPage(true, response, error);
                }
                else {
                    response.render('maintenance/UpdateSPOCDL.ejs', {
                        SelectedTeam: SelectedTeam,
                        TeamList: results[0],
                        SPOCDLDetails: results[1],
                        Result: request.Result
                    });
                }
            });
    },

    SaveSPOCAndDL: function (request, response, next) {
        var SelectedTeam = request.body.Team;
        var SPOCEmailId = request.body.SPOCEmailId;
        var GroupDL = AppUtil.checkNull(request.body.GroupDL);
        maintenancemodel.SaveSPOCAndDL(SelectedTeam, SPOCEmailId, GroupDL, function (error) {
            if (error) {
                AppUtil.renderErrorPage(true, response, error);
            }
            else {
                request.Result = AppConstants.SUCCESS;
                request.url = '/loadSPOCAndDL';
                next('route');
            }
        });
    },

    loadAddLocation: function (request, response, next) {
        async.parallel([
                function (callback) {
                    maintenancemodel.getActiveLocations(function (error, ActiveLocations) {
                        callback(error, ActiveLocations);
                    });
                },
                function (callback) {
                    maintenancemodel.getInActiveLocations(function (error, InActiveLocations) {
                        callback(error, InActiveLocations);
                    });
                }
            ],
            function (error, results) {
                if (error) {
                    AppUtil.renderErrorPage(true, response, error);
                }
                else {
                    response.render('maintenance/AddLocation.ejs', {
                        ActiveLocations: results[0],
                        InActiveLocations: results[1],
                        LocationName: request.LocationName,
                        Result: request.Result
                    });
                }
            });
    },

    AddLocation: function (request, response, next) {
        var LocationName = AppUtil.checkNull(request.body.LocationName);
        maintenancemodel.checkLocationExists(LocationName, function (error, LocationExists) {
            if (error) {
                AppUtil.renderErrorPage(true, response, error);
            }
            else if (LocationExists) {
                request.LocationName = LocationName;
                request.Result = "EXISTS";
                request.url = '/loadAddLocation';
                next('route');
            }
            else {
                maintenancemodel.AddLocation(LocationName, function (error) {
                    if (error) {
                        AppUtil.renderErrorPage(true, response, error);
                    }
                    else {
                        request.Result = AppConstants.SUCCESS;
                        request.url = '/loadAddLocation';
                        next('route');
                    }
                });
            }
        });
    },

    loadUpdateLocation: function (request, response, next) {
        maintenancemodel.getLocationDetails(request.body.hidRefNo, function (error, LocationDetails) {
            if (error) {
                AppUtil.renderErrorPage(false, response, error);
            }
            else {
                response.render('maintenance/UpdateLocation.ejs', {
                    LocationDetails: LocationDetails,
                    Result: request.Result
                });
            }
        });
    },

    UpdateLocation: function (request, response, next) {
        var RefNo = request.body.hidRefNo;
        var LocationName = AppUtil.checkNull(request.body.LocationName);
        var OldLocationName = AppUtil.checkNull(request.body.hidOldLocationName);
        var ActiveFlag = request.body.ActiveFlag;
        maintenancemodel.checkLocationExistsForUpdate(LocationName, OldLocationName, function (error, LocationExists) {
            if (error) {
                AppUtil.renderErrorPage(false, response, error);
            }
            else if (LocationExists) {
                request.Result = "EXISTS";
                request.url = '/loadUpdateLocation';
                next('route');
            }
            else {
                maintenancemodel.UpdateLocation(LocationName, ActiveFlag, RefNo, function (error) {
                    if (error) {
                        AppUtil.renderErrorPage(true, response, error);
                    }
                    else {
                        request.Result = AppConstants.SUCCESS;
                        request.url = '/loadUpdateLocation';
                        next('route');
                    }
                });
            }
        });
    },

    loadAddVendor: function (request, response, next) {
        async.parallel([
                function (callback) {
                    maintenancemodel.getActiveVendors(function (error, ActiveVendors) {
                        callback(error, ActiveVendors);
                    });
                },
                function (callback) {
                    maintenancemodel.getInActiveVendors(function (error, InActiveVendors) {
                        callback(error, InActiveVendors);
                    });
                }
            ],
            function (error, results) {
                if (error) {
                    AppUtil.renderErrorPage(true, response, error);
                }
                else {
                    response.render('maintenance/AddVendor.ejs', {
                        ActiveVendors: results[0],
                        InActiveVendors: results[1],
                        VendorName: request.VendorName,
                        Result: request.Result
                    });
                }
            });
    },

    AddVendor: function (request, response, next) {
        var VendorName = AppUtil.checkNull(request.body.VendorName);
        maintenancemodel.checkVendorExists(VendorName, function (error, VendorExists) {
            if (error) {
                AppUtil.renderErrorPage(true, response, error);
            }
            else if (VendorExists) {
                request.VendorName = VendorName;
                request.Result = "EXISTS";
                request.url = '/loadAddVendor';
                next('route');
            }
            else {
                maintenancemodel.AddVendor(VendorName, function (error) {
                    if (error) {
                        AppUtil.renderErrorPage(true, response, error);
                    }
                    else {
                        request.Result = AppConstants.SUCCESS;
                        request.url = '/loadAddVendor';
                        next('route');
                    }
                });
            }
        });
    },

    loadUpdateVendor: function (request, response, next) {
        maintenancemodel.getVendorDetails(request.body.hidRefNo, function (error, VendorDetails) {
            if (error) {
                AppUtil.renderErrorPage(false, response, error);
            }
            else {
                response.render('maintenance/UpdateVendor.ejs', {
                    VendorDetails: VendorDetails,
                    Result: request.Result
                });
            }
        });
    },

    UpdateVendor: function (request, response, next) {
        var RefNo = request.body.hidRefNo;
        var VendorName = AppUtil.checkNull(request.body.VendorName);
        var OldVendorName = AppUtil.checkNull(request.body.hidOldVendorName);
        var ActiveFlag = request.body.ActiveFlag;
        maintenancemodel.checkVendorExistsForUpdate(VendorName, OldVendorName, function (error, VendorExists) {
            if (error) {
                AppUtil.renderErrorPage(false, response, error);
            }
            else if (VendorExists) {
                request.Result = "EXISTS";
                request.url = '/loadUpdateVendor';
                next('route');
            }
            else {
                maintenancemodel.UpdateVendor(VendorName, ActiveFlag, RefNo, function (error) {
                    if (error) {
                        AppUtil.renderErrorPage(true, response, error);
                    }
                    else {
                        request.Result = AppConstants.SUCCESS;
                        request.url = '/loadUpdateVendor';
                        next('route');
                    }
                });
            }
        });
    },

    loadAddSkillSet: function (request, response, next) {
        async.parallel([
                function (callback) {
                    maintenancemodel.getActiveSkillSets(function (error, ActiveSkillSets) {
                        callback(error, ActiveSkillSets);
                    });
                },
                function (callback) {
                    maintenancemodel.getInActiveSkillSets(function (error, InActiveSkillSets) {
                        callback(error, InActiveSkillSets);
                    });
                }
            ],
            function (error, results) {
                if (error) {
                    AppUtil.renderErrorPage(true, response, error);
                }
                else {
                    response.render('maintenance/AddSkillSet.ejs', {
                        ActiveSkillSets: results[0],
                        InActiveSkillSets: results[1],
                        SkillSetName: request.SkillSetName,
                        Result: request.Result
                    });
                }
            });
    },

    AddSkillSet: function (request, response, next) {
        var SkillSetName = AppUtil.checkNull(request.body.SkillSetName);
        maintenancemodel.checkSkillSetExists(SkillSetName, function (error, SkillSetExists) {
            if (error) {
                AppUtil.renderErrorPage(true, response, error);
            }
            else if (SkillSetExists) {
                request.SkillSetName = SkillSetName;
                request.Result = "EXISTS";
                request.url = '/loadAddSkillSet';
                next('route');
            }
            else {
                maintenancemodel.AddSkillSet(SkillSetName, function (error) {
                    if (error) {
                        AppUtil.renderErrorPage(true, response, error);
                    }
                    else {
                        request.Result = AppConstants.SUCCESS;
                        request.url = '/loadAddSkillSet';
                        next('route');
                    }
                });
            }
        });
    },

    loadUpdateSkillSet: function (request, response, next) {
        maintenancemodel.getSkillSetDetails(request.body.hidRefNo, function (error, SkillSetDetails) {
            if (error) {
                AppUtil.renderErrorPage(false, response, error);
            }
            else {
                response.render('maintenance/UpdateSkillSet.ejs', {
                    SkillSetDetails: SkillSetDetails,
                    Result: request.Result
                });
            }
        });
    },

    UpdateSkillSet: function (request, response, next) {
        var RefNo = request.body.hidRefNo;
        var SkillSetName = AppUtil.checkNull(request.body.SkillSetName);
        var OldSkillSetName = AppUtil.checkNull(request.body.hidOldSkillSetName);
        var ActiveFlag = request.body.ActiveFlag;
        maintenancemodel.checkSkillSetExistsForUpdate(SkillSetName, OldSkillSetName, function (error, SkillSetExists) {
            if (error) {
                AppUtil.renderErrorPage(false, response, error);
            }
            else if (SkillSetExists) {
                request.Result = "EXISTS";
                request.url = '/loadUpdateSkillSet';
                next('route');
            }
            else {
                maintenancemodel.UpdateSkillSet(SkillSetName, ActiveFlag, RefNo, function (error) {
                    if (error) {
                        AppUtil.renderErrorPage(true, response, error);
                    }
                    else {
                        request.Result = AppConstants.SUCCESS;
                        request.url = '/loadUpdateSkillSet';
                        next('route');
                    }
                });
            }
        });
    },

    loadAddDesigTitle: function (request, response, next) {
        async.parallel([
                function (callback) {
                    maintenancemodel.getActiveDesigTitles(function (error, ActiveDesigTitles) {
                        callback(error, ActiveDesigTitles);
                    });
                },
                function (callback) {
                    maintenancemodel.getInActiveDesigTitles(function (error, InActiveDesigTitles) {
                        callback(error, InActiveDesigTitles);
                    });
                }
            ],
            function (error, results) {
                if (error) {
                    AppUtil.renderErrorPage(true, response, error);
                }
                else {
                    response.render('maintenance/AddDesigTitle.ejs', {
                        ActiveDesigTitles: results[0],
                        InActiveDesigTitles: results[1],
                        DesigTitleName: request.DesigTitleName,
                        Result: request.Result
                    });
                }
            });
    },

    AddDesigTitle: function (request, response, next) {
        var DesigTitleName = AppUtil.checkNull(request.body.DesigTitleName);
        maintenancemodel.checkDesigTitleExists(DesigTitleName, function (error, DesigTitleExists) {
            if (error) {
                AppUtil.renderErrorPage(true, response, error);
            }
            else if (DesigTitleExists) {
                request.DesigTitleName = DesigTitleName;
                request.Result = "EXISTS";
                request.url = '/loadAddDesigTitle';
                next('route');
            }
            else {
                maintenancemodel.AddDesigTitle(DesigTitleName, function (error) {
                    if (error) {
                        AppUtil.renderErrorPage(true, response, error);
                    }
                    else {
                        request.Result = AppConstants.SUCCESS;
                        request.url = '/loadAddDesigTitle';
                        next('route');
                    }
                });
            }
        });
    },

    loadUpdateDesigTitle: function (request, response, next) {
        maintenancemodel.getDesigTitleDetails(request.body.hidRefNo, function (error, DesigTitleDetails) {
            if (error) {
                AppUtil.renderErrorPage(false, response, error);
            }
            else {
                response.render('maintenance/UpdateDesigTitle.ejs', {
                    DesigTitleDetails: DesigTitleDetails,
                    Result: request.Result
                });
            }
        });
    },

    UpdateDesigTitle: function (request, response, next) {
        var RefNo = request.body.hidRefNo;
        var DesigTitleName = AppUtil.checkNull(request.body.DesigTitleName);
        var OldDesigTitleName = AppUtil.checkNull(request.body.hidOldDesigTitleName);
        var ActiveFlag = request.body.ActiveFlag;
        maintenancemodel.checkDesigTitleExistsForUpdate(DesigTitleName, OldDesigTitleName, function (error, DesigTitleExists) {
            if (error) {
                AppUtil.renderErrorPage(false, response, error);
            }
            else if (DesigTitleExists) {
                request.Result = "EXISTS";
                request.url = '/loadUpdateDesigTitle';
                next('route');
            }
            else {
                maintenancemodel.UpdateDesigTitle(DesigTitleName, ActiveFlag, RefNo, function (error) {
                    if (error) {
                        AppUtil.renderErrorPage(true, response, error);
                    }
                    else {
                        request.Result = AppConstants.SUCCESS;
                        request.url = '/loadUpdateDesigTitle';
                        next('route');
                    }
                });
            }
        });
    },

    loadAddProjectRole: function (request, response, next) {
        async.parallel([
                function (callback) {
                    maintenancemodel.getActiveProjectRoles(function (error, ActiveProjectRoles) {
                        callback(error, ActiveProjectRoles);
                    });
                },
                function (callback) {
                    maintenancemodel.getInActiveProjectRoles(function (error, InActiveProjectRoles) {
                        callback(error, InActiveProjectRoles);
                    });
                }
            ],
            function (error, results) {
                if (error) {
                    AppUtil.renderErrorPage(true, response, error);
                }
                else {
                    response.render('maintenance/AddProjectRole.ejs', {
                        ActiveProjectRoles: results[0],
                        InActiveProjectRoles: results[1],
                        ProjectRoleName: request.ProjectRoleName,
                        Result: request.Result
                    });
                }
            });
    },

    AddProjectRole: function (request, response, next) {
        var ProjectRoleName = AppUtil.checkNull(request.body.ProjectRoleName);
        maintenancemodel.checkProjectRoleExists(ProjectRoleName, function (error, ProjectRoleExists) {
            if (error) {
                AppUtil.renderErrorPage(true, response, error);
            }
            else if (ProjectRoleExists) {
                request.ProjectRoleName = ProjectRoleName;
                request.Result = "EXISTS";
                request.url = '/loadAddProjectRole';
                next('route');
            }
            else {
                maintenancemodel.AddProjectRole(ProjectRoleName, function (error) {
                    if (error) {
                        AppUtil.renderErrorPage(true, response, error);
                    }
                    else {
                        request.Result = AppConstants.SUCCESS;
                        request.url = '/loadAddProjectRole';
                        next('route');
                    }
                });
            }
        });
    },

    loadUpdateProjectRole: function (request, response, next) {
        maintenancemodel.getProjectRoleDetails(request.body.hidRefNo, function (error, ProjectRoleDetails) {
            if (error) {
                AppUtil.renderErrorPage(false, response, error);
            }
            else {
                response.render('maintenance/UpdateProjectRole.ejs', {
                    ProjectRoleDetails: ProjectRoleDetails,
                    Result: request.Result
                });
            }
        });
    },

    UpdateProjectRole: function (request, response, next) {
        var RefNo = request.body.hidRefNo;
        var ProjectRoleName = AppUtil.checkNull(request.body.ProjectRoleName);
        var OldProjectRoleName = AppUtil.checkNull(request.body.hidOldProjectRoleName);
        var ActiveFlag = request.body.ActiveFlag;
        maintenancemodel.checkProjectRoleExistsForUpdate(ProjectRoleName, OldProjectRoleName, function (error, ProjectRoleExists) {
            if (error) {
                AppUtil.renderErrorPage(false, response, error);
            }
            else if (ProjectRoleExists) {
                request.Result = "EXISTS";
                request.url = '/loadUpdateProjectRole';
                next('route');
            }
            else {
                maintenancemodel.UpdateProjectRole(ProjectRoleName, ActiveFlag, RefNo, function (error) {
                    if (error) {
                        AppUtil.renderErrorPage(true, response, error);
                    }
                    else {
                        request.Result = AppConstants.SUCCESS;
                        request.url = '/loadUpdateProjectRole';
                        next('route');
                    }
                });
            }
        });
    },


    loadAddClientEng: function (request, response, next) {
        async.parallel([
                function (callback) {
                    maintenancemodel.getActiveClientEngs(function (error, ActiveClientEngs) {
                        callback(error, ActiveClientEngs);
                    });
                },
                function (callback) {
                    maintenancemodel.getInActiveClientEngs(function (error, InActiveClientEngs) {
                        callback(error, InActiveClientEngs);
                    });
                }
            ],
            function (error, results) {
                if (error) {
                    AppUtil.renderErrorPage(true, response, error);
                }
                else {
                    response.render('maintenance/AddClientEng.ejs', {
                        ActiveClientEngs: results[0],
                        InActiveClientEngs: results[1],
                        ClientEngName: request.ClientEngName,
                        Result: request.Result
                    });
                }
            });
    },

    AddClientEng: function (request, response, next) {
        var ClientEngName = AppUtil.checkNull(request.body.ClientEngName);
        maintenancemodel.checkClientEngExists(ClientEngName, function (error, ClientEngExists) {
            if (error) {
                AppUtil.renderErrorPage(true, response, error);
            }
            else if (ClientEngExists) {
                request.ClientEngName = ClientEngName;
                request.Result = "EXISTS";
                request.url = '/loadAddClientEng';
                next('route');
            }
            else {
                maintenancemodel.AddClientEng(ClientEngName, function (error) {
                    if (error) {
                        AppUtil.renderErrorPage(true, response, error);
                    }
                    else {
                        request.Result = AppConstants.SUCCESS;
                        request.url = '/loadAddClientEng';
                        next('route');
                    }
                });
            }
        });
    },

    loadUpdateClientEng: function (request, response, next) {
        maintenancemodel.getClientEngDetails(request.body.hidRefNo, function (error, ClientEngDetails) {
            if (error) {
                AppUtil.renderErrorPage(false, response, error);
            }
            else {
                response.render('maintenance/UpdateClientEng.ejs', {
                    ClientEngDetails: ClientEngDetails,
                    Result: request.Result
                });
            }
        });
    },

    UpdateClientEng: function (request, response, next) {
        var RefNo = request.body.hidRefNo;
        var ClientEngName = AppUtil.checkNull(request.body.ClientEngName);
        var OldClientEngName = AppUtil.checkNull(request.body.hidOldClientEngName);
        var ActiveFlag = request.body.ActiveFlag;
        maintenancemodel.checkClientEngExistsForUpdate(ClientEngName, OldClientEngName, function (error, ClientEngExists) {
            if (error) {
                AppUtil.renderErrorPage(false, response, error);
            }
            else if (ClientEngExists) {
                request.Result = "EXISTS";
                request.url = '/loadUpdateClientEng';
                next('route');
            }
            else {
                maintenancemodel.UpdateClientEng(ClientEngName, ActiveFlag, RefNo, function (error) {
                    if (error) {
                        AppUtil.renderErrorPage(true, response, error);
                    }
                    else {
                        request.Result = AppConstants.SUCCESS;
                        request.url = '/loadUpdateClientEng';
                        next('route');
                    }
                });
            }
        });
    },

    loadAddProjectTeam: function (request, response, next) {
        async.parallel([
                function (callback) {
                    maintenancemodel.getActiveProjectTeams(function (error, ActiveProjectTeams) {
                        callback(error, ActiveProjectTeams);
                    });
                },
                function (callback) {
                    maintenancemodel.getInActiveProjectTeams(function (error, InActiveProjectTeams) {
                        callback(error, InActiveProjectTeams);
                    });
                }
            ],
            function (error, results) {
                if (error) {
                    AppUtil.renderErrorPage(true, response, error);
                }
                else {
                    response.render('maintenance/AddProjectTeam.ejs', {
                        ActiveProjectTeams: results[0],
                        InActiveProjectTeams: results[1],
                        ProjectTeamName: request.ProjectTeamName,
                        Result: request.Result
                    });
                }
            });
    },

    AddProjectTeam: function (request, response, next) {
        var ProjectTeamName = AppUtil.checkNull(request.body.ProjectTeamName);
        maintenancemodel.checkProjectTeamExists(ProjectTeamName, function (error, ProjectTeamExists) {
            if (error) {
                AppUtil.renderErrorPage(true, response, error);
            }
            else if (ProjectTeamExists) {
                request.ProjectTeamName = ProjectTeamName;
                request.Result = "EXISTS";
                request.url = '/loadAddProjectTeam';
                next('route');
            }
            else {
                maintenancemodel.AddProjectTeam(ProjectTeamName, function (error) {
                    if (error) {
                        AppUtil.renderErrorPage(true, response, error);
                    }
                    else {
                        request.Result = AppConstants.SUCCESS;
                        request.url = '/loadAddProjectTeam';
                        next('route');
                    }
                });
            }
        });
    },

    loadUpdateProjectTeam: function (request, response, next) {
        maintenancemodel.getProjectTeamDetails(request.body.hidRefNo, function (error, ProjectTeamDetails) {
            if (error) {
                AppUtil.renderErrorPage(false, response, error);
            }
            else {
                response.render('maintenance/UpdateProjectTeam.ejs', {
                    ProjectTeamDetails: ProjectTeamDetails,
                    Result: request.Result
                });
            }
        });
    },

    UpdateProjectTeam: function (request, response, next) {
        var RefNo = request.body.hidRefNo;
        var ProjectTeamName = AppUtil.checkNull(request.body.ProjectTeamName);
        var OldProjectTeamName = AppUtil.checkNull(request.body.hidOldProjectTeamName);
        var ActiveFlag = request.body.ActiveFlag;
        maintenancemodel.checkProjectTeamExistsForUpdate(ProjectTeamName, OldProjectTeamName, function (error, ProjectTeamExists) {
            if (error) {
                AppUtil.renderErrorPage(false, response, error);
            }
            else if (ProjectTeamExists) {
                request.Result = "EXISTS";
                request.url = '/loadUpdateProjectTeam';
                next('route');
            }
            else {
                maintenancemodel.UpdateProjectTeam(ProjectTeamName, ActiveFlag, RefNo, function (error) {
                    if (error) {
                        AppUtil.renderErrorPage(true, response, error);
                    }
                    else {
                        request.Result = AppConstants.SUCCESS;
                        request.url = '/loadUpdateProjectTeam';
                        next('route');
                    }
                });
            }
        });
    },

    loadAddCOEType: function (request, response, next) {
        async.parallel([
                function (callback) {
                    maintenancemodel.getActiveCOETypes(function (error, ActiveCOETypes) {
                        callback(error, ActiveCOETypes);
                    });
                },
                function (callback) {
                    maintenancemodel.getInActiveCOETypes(function (error, InActiveCOETypes) {
                        callback(error, InActiveCOETypes);
                    });
                }
            ],
            function (error, results) {
                if (error) {
                    AppUtil.renderErrorPage(true, response, error);
                }
                else {
                    response.render('maintenance/AddCOEType.ejs', {
                        ActiveCOETypes: results[0],
                        InActiveCOETypes: results[1],
                        COETypeName: request.COETypeName,
                        Result: request.Result
                    });
                }
            });
    },

    AddCOEType: function (request, response, next) {
        var COETypeName = AppUtil.checkNull(request.body.COETypeName);
        maintenancemodel.checkCOETypeExists(COETypeName, function (error, COETypeExists) {
            if (error) {
                AppUtil.renderErrorPage(true, response, error);
            }
            else if (COETypeExists) {
                request.COETypeName = COETypeName;
                request.Result = "EXISTS";
                request.url = '/loadAddCOEType';
                next('route');
            }
            else {
                maintenancemodel.AddCOEType(COETypeName, function (error) {
                    if (error) {
                        AppUtil.renderErrorPage(true, response, error);
                    }
                    else {
                        request.Result = AppConstants.SUCCESS;
                        request.url = '/loadAddCOEType';
                        next('route');
                    }
                });
            }
        });
    },

    loadUpdateCOEType: function (request, response, next) {
        maintenancemodel.getCOETypeDetails(request.body.hidRefNo, function (error, COETypeDetails) {
            if (error) {
                AppUtil.renderErrorPage(false, response, error);
            }
            else {
                response.render('maintenance/UpdateCOEType.ejs', {
                    COETypeDetails: COETypeDetails,
                    Result: request.Result
                });
            }
        });
    },

    UpdateCOEType: function (request, response, next) {
        var RefNo = request.body.hidRefNo;
        var COETypeName = AppUtil.checkNull(request.body.COETypeName);
        var OldCOETypeName = AppUtil.checkNull(request.body.hidOldCOETypeName);
        var ActiveFlag = request.body.ActiveFlag;
        maintenancemodel.checkCOETypeExistsForUpdate(COETypeName, OldCOETypeName, function (error, COETypeExists) {
            if (error) {
                AppUtil.renderErrorPage(false, response, error);
            }
            else if (COETypeExists) {
                request.Result = "EXISTS";
                request.url = '/loadUpdateCOEType';
                next('route');
            }
            else {
                maintenancemodel.UpdateCOEType(COETypeName, ActiveFlag, RefNo, function (error) {
                    if (error) {
                        AppUtil.renderErrorPage(true, response, error);
                    }
                    else {
                        request.Result = AppConstants.SUCCESS;
                        request.url = '/loadUpdateCOEType';
                        next('route');
                    }
                });
            }
        });
    },

    loadAddVisaType: function (request, response, next) {
        async.parallel([
                function (callback) {
                    maintenancemodel.getActiveVisaTypes(function (error, ActiveVisaTypes) {
                        callback(error, ActiveVisaTypes);
                    });
                },
                function (callback) {
                    maintenancemodel.getInActiveVisaTypes(function (error, InActiveVisaTypes) {
                        callback(error, InActiveVisaTypes);
                    });
                }
            ],
            function (error, results) {
                if (error) {
                    AppUtil.renderErrorPage(true, response, error);
                }
                else {
                    response.render('maintenance/AddVisaType.ejs', {
                        ActiveVisaTypes: results[0],
                        InActiveVisaTypes: results[1],
                        VisaTypeName: request.VisaTypeName,
                        Result: request.Result
                    });
                }
            });
    },

    AddVisaType: function (request, response, next) {
        var VisaTypeName = AppUtil.checkNull(request.body.VisaTypeName);
        maintenancemodel.checkVisaTypeExists(VisaTypeName, function (error, VisaTypeExists) {
            if (error) {
                AppUtil.renderErrorPage(true, response, error);
            }
            else if (VisaTypeExists) {
                request.VisaTypeName = VisaTypeName;
                request.Result = "EXISTS";
                request.url = '/loadAddVisaType';
                next('route');
            }
            else {
                maintenancemodel.AddVisaType(VisaTypeName, function (error) {
                    if (error) {
                        AppUtil.renderErrorPage(true, response, error);
                    }
                    else {
                        request.Result = AppConstants.SUCCESS;
                        request.url = '/loadAddVisaType';
                        next('route');
                    }
                });
            }
        });
    },

    loadUpdateVisaType: function (request, response, next) {
        maintenancemodel.getVisaTypeDetails(request.body.hidRefNo, function (error, VisaTypeDetails) {
            if (error) {
                AppUtil.renderErrorPage(false, response, error);
            }
            else {
                response.render('maintenance/UpdateVisaType.ejs', {
                    VisaTypeDetails: VisaTypeDetails,
                    Result: request.Result
                });
            }
        });
    },

    UpdateVisaType: function (request, response, next) {
        var RefNo = request.body.hidRefNo;
        var VisaTypeName = AppUtil.checkNull(request.body.VisaTypeName);
        var OldVisaTypeName = AppUtil.checkNull(request.body.hidOldVisaTypeName);
        var ActiveFlag = request.body.ActiveFlag;
        maintenancemodel.checkVisaTypeExistsForUpdate(VisaTypeName, OldVisaTypeName, function (error, VisaTypeExists) {
            if (error) {
                AppUtil.renderErrorPage(false, response, error);
            }
            else if (VisaTypeExists) {
                request.Result = "EXISTS";
                request.url = '/loadUpdateVisaType';
                next('route');
            }
            else {
                maintenancemodel.UpdateVisaType(VisaTypeName, ActiveFlag, RefNo, function (error) {
                    if (error) {
                        AppUtil.renderErrorPage(true, response, error);
                    }
                    else {
                        request.Result = AppConstants.SUCCESS;
                        request.url = '/loadUpdateVisaType';
                        next('route');
                    }
                });
            }
        });
    },


};
