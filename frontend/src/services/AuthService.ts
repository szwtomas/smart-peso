import { HttpService } from "./HttpService";
import { HTTP_STATUS_CONFLICT, HTTP_STATUS_CREATED, HTTP_STATUS_NOT_FOUND, HTTP_STATUS_OK } from "./httpConstants";

export interface SignUpRequestBody {
    email: string;
    password: string;
    firstName: string;
    lastName: string;
}

export interface AuthenticationResponse {
    accessToken: string;
    userId: number;
    email: string;
    firstName: string;
    lastName: string;
}

export interface UserData {
    userId: number;
    firstname: string;
    lastname: string;
    email: string;
}

export class AuthService {
    private httpService: HttpService;

    constructor(httpService: HttpService) {
        this.httpService = httpService;
    }

    public async logIn(email: string, password: string): Promise<AuthenticationResponse> {
        const response = await this.httpService.post("/api/auth/log-in", { email, password });
        if (response.status === HTTP_STATUS_NOT_FOUND) {
            throw new Error("mail/contraseña invalido/s");
        } else if (response.status !== HTTP_STATUS_OK) {
            throw new Error("Inicio de sesión falló inesperadamente");
        }
        
        const accessToken: string = (await response.json()).accessToken;
        this.httpService.setAccessToken(accessToken);
        const userData = await this.getUserData();

        return {
            accessToken,
            userId: userData.userId,
            email: userData.email,
            firstName: userData.firstname,
            lastName: userData.lastname,
        };
    }

    public async signUp(signUpData: SignUpRequestBody): Promise<AuthenticationResponse> {
        const response = await this.httpService.post("/api/auth/sign-up", signUpData);
        if (response.status === HTTP_STATUS_CONFLICT) {
            console.error(await response.text());
            throw new Error("El usuario ya existe");
        } else if (response.status !== HTTP_STATUS_CREATED) {
            console.error(await response.text());
            throw new Error("La creación del usuario falló inesperadamente");
        }

        const accessToken: string = (await response.json()).accessToken;
        this.httpService.setAccessToken(accessToken);
        const userData = await this.getUserData();

        return {
            accessToken,
            userId: userData.userId,
            email: userData.email,
            firstName: userData.firstname,
            lastName: userData.lastname,
        };
    }

    private async getUserData(): Promise<UserData> {
        const response = await this.httpService.get("/api/auth/user");
        if (response.status !== HTTP_STATUS_OK) {
            console.error(await response.text());
            throw new Error("Error inesperado al obtener los datos del usuario");
        }

        return (await response.json()) as unknown as UserData;
    }
}
