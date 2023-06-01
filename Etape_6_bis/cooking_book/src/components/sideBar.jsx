import { Link } from "react-router-dom";
import {
    Avatar,
    Drawer,
    List,
    ListItem,
    ListItemButton,
    ListItemText,
    Stack,
    Toolbar
} from "@mui/material";

import logo from "../assets/logo192.png";
import { logout } from "../utils/logout";

function SideBar() {
    let base = "http://localhost:3000";
    let appRoutes = [
        {
            name: "dashboard",
            route: base + "/home",
            todo: "redirect",
        },
        {
            name: "addresses",
            route: base + "/addresses",
            todo: "redirect",
        },
        {
            name: "profile",
            route: base + "/profile",
            todo: "redirect",
        },
        {
            name: "logout",
            route: base,
            todo: "logout",
        }
    ];

    return ( 
        <Drawer
            variant="permanent"
            sx={{
                width: "200px",
                flexShrink: 0,
                "& .MuiDrawer-paper": {
                width: "300px",
                boxSizing: "border-box",
                borderRight: "0px",
                backgroundColor: "#233044",
                color: "#eeeeee",
                }
            }}
        >
            <List disablePadding>
                <Toolbar sx={{ marginBottom: "40px", marginTop: "20px" }}>
                <Stack
                    sx={{ width: "100%" }}
                    direction="row"
                    justifyContent="center"
                >
                    <Avatar src={logo} />
                </Stack>
                </Toolbar>
                { appRoutes.map((route, index) => ( <SidebarItem item={route} key={index} /> )) }
            </List>
        </Drawer>
     );
}

export default SideBar;

export function SidebarItem(props) {
    return ( 
        <ListItem>
            {props.item.todo === "redirect"
            ? (
                <ListItemButton
                    component={Link}
                    to={props.item.route}
                >
                    <ListItemText primary={props.item.name} />
                </ListItemButton>
            )
            : (
                <ListItemButton
                    onClick={() => logout()}
                >
                    <ListItemText primary={props.item.name} />
                </ListItemButton>
            )}
            
        </ListItem>
    );
}