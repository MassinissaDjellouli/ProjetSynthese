import { Injectable } from '@angular/core';
import { ApiError } from '../../interfaces/ApiError';
import { ApiResponse } from 'src/app/interfaces/ApiResponse';

@Injectable({
  providedIn: 'root'
})
export class RequestService {

  constructor() { }
  private apiLink = "http://localhost:8000/api/"

  getRequest = async (endpoint: string): Promise<ApiResponse | ApiError> => {
    const res = await fetch(this.apiLink + endpoint)
    if (res.ok) {
      return { data: await res.json() } as ApiResponse
    }
    return { error: await res.text() } as ApiError
  }

  putRequest = async <T> (endpoint: string,body:T): Promise<ApiResponse | ApiError> => {
    const res = await fetch(this.apiLink + endpoint,{
      method:"PUT",
      body:JSON.stringify(body)
    })
    if (res.ok) {
      return { data: await res.json() } as ApiResponse
    }
    return { error: await res.text() } as ApiError
  }
}
export const isError = (res: ApiError | ApiResponse) => {
  try {
    return 'error' in res;
  } catch {
    return false;
  }
}
export const parseError = (res:ApiError | ApiResponse) => {
  let err = (res as ApiError).error
  return err;
}
