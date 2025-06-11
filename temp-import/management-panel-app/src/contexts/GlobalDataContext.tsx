import React, { createContext, useContext, useState } from "react";

interface GlobalData {
    users: any[];
    servers: any[];
    connections: any[];
    plans: any[];
    subscriptions: any[];
    payments: any[];
}

interface GlobalDataContextType {
    data: GlobalData;
    setData: React.Dispatch<React.SetStateAction<GlobalData>>;
}

const defaultData: GlobalData = {
    users: [],
    servers: [],
    connections: [],
    plans: [],
    subscriptions: [],
    payments: [],
};

const GlobalDataContext = createContext<GlobalDataContextType | undefined>(undefined);

export const GlobalDataProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [data, setData] = useState<GlobalData>(defaultData);

    return (
        <GlobalDataContext.Provider value={{ data, setData }}>
            {children}
        </GlobalDataContext.Provider>
    );
};

export function useGlobalDataContext() {
    const context = useContext(GlobalDataContext);
    if (!context) throw new Error("useGlobalDataContext must be used within a GlobalDataProvider");
    return context;
}