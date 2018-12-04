

import socket, sys, threading, json,time,os,ssl
import os.path
import glob
import json
import optparse
import select


#Validate the IP address of the correct format
def validate_ip(s):
    """
    Arguments:
    s -- dot decimal IP address in string

    Returns:
    True if valid; False otherwise
    """

    a = s.split('.')
    if len(a) != 4:
        return False
    for x in a:
        if not x.isdigit():
            return False
        i = int(x)
        if i < 0 or i > 255:
            return False
    return True

#Validate the port number is in range [0, 2^16-1]
def validate_port(x):
    """
    Arguments:
    x -- port number

    Returns:
    True if valid; False, otherwise
    """

    if not x.isdigit():
        return False
    i = int(x)
    if i < 0 or i > 65535:
            return False
    return True


#Get file info in the local directory (subdirectories are ignored)
#NOTE: Exclude files with .so, .py, .dll suffixes
def get_file_info(port):
    """
    Return: a JSON array of {"name":file,"mtime":mtime}
    """
    files = []
    for fname in os.listdir('.'): #check current directory
        if(os.path.isdir(fname)==False): #check that file is not a directory
            ext = os.path.splitext(fname)[1]
            if(ext != ".so" and ext!=".py" and ext!=".dll"): # check for extensions
                files.append({'name':fname,'mtime':os.path.getmtime(fname)})
    return json.dumps({'files':files,'port':port})

#Check if a port is available
def check_port_avaliable(check_port):
    """
    Arguments:
    check_port -- port number

    Returns:
    True if valid; False otherwise
    """
    if str(check_port) in os.popen("netstat -na").read():
        return False
    return True

#Get the next available port by searching from initial_port to 2^16 - 1
#Hint: use check_port_avaliable() function
def get_next_avaliable_port(initial_port):
    for i in range(initial_port,2**16):
        if(check_port_avaliable(i)):
            return i
    return False
    """

    Arguments:
    initial_port -- the first port to check

    Return:
    port found to be available; False if no port is available.
    """

    # YOUR CODE


class FileSynchronizer(threading.Thread):
    def __init__(self, trackerhost,trackerport,port, host='0.0.0.0'):

        threading.Thread.__init__(self)
        #Port for serving file request
        self.port = port
        self.host = host

        #Tracker IP/hostname and port
        self.trackerhost = trackerhost
        self.trackerport = trackerport

        self.BUFFER_SIZE = 8192

        #Create a TCP socket to communicate with tracker
        self.client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.client.settimeout(180)


        #Store the message to be sent to tracker. Initialize to Init message
        #that contains port number and local file info.
        self.msg = get_file_info(self.port)

        #Create a TCP socket to serve file requests
        self.server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

        try:
            self.server.bind((self.host, self.port))
        except socket.error:
            print('Bind failed %s' % (socket.error))
            sys.exit()
        self.server.listen(10)

    # Not currently used. Ensure sockets are closed on disconnect
    def exit(self):
        self.server.close()

    #Handle file request from a peer
    def process_message(self, conn,addr):
        """
        Arguments:
        self -- self object
        conn -- socket object for an accepted connection from a peer
        addr -- address bound to the socket of the accepted connection
        """
        data = ''
        while True: # recieve file name
            part = conn.recv(self.BUFFER_SIZE)
            data =data+ part
            if len(part) < self.BUFFER_SIZE:
                break
        if len(data)>0: #if file name is not empty
            data_dic=json.loads(data)
            for name in data_dic: #send file
                send = open(name,"rb")
                fread = send.read(1024)
                while fread:
                    conn.send(fread)
                    fread = send.read(1024)
        conn.close()


    def run(self):
        self.client.connect((self.trackerhost,self.trackerport))
        t = threading.Timer(2, self.sync)
        t.start()
        print('Waiting for connections on port %s' % (self.port))

        while True:
            conn, addr = self.server.accept()
            threading.Thread(target=self.process_message, args=(conn,addr)).start()

    #Send Init or KeepAlive message to tracker, handle directory response message
    #and call self.syncfile() to request files from peers
    def sync(self):
        print 'connect to: '+self.trackerhost,self.trackerport
        #Step 1. send Init msg to tracker
        #YOUR CODE
        self.client.send(self.msg) #send init message
        #Step 2. receive a directory response message from tracker
        directory_response_message = ''
        while True: # get directory_response_message
            part = self.client.recv(self.BUFFER_SIZE)
            directory_response_message =directory_response_message+ part
            if len(part) < self.BUFFER_SIZE:
                break
        mes = json.loads(directory_response_message)
  


        request =[]
        exists = False
        for fname in mes: #parse through directory_response_message
            exists = False
            for locname in os.listdir('.'):
                if(fname==locname): #if file is in local
                    exists=True
                    if(mes[fname]['mtime']!=os.path.getmtime(locname)): # if modified times are different
                        request.append(fname)
            if(exists==False): # if file is not in local
                request.append(fname)

            if len(request)>0: #if there are files to request

                client1 = socket.socket(socket.AF_INET, socket.SOCK_STREAM) #create new socket for peer
                client1.settimeout(30)
                client1.connect((mes[request[0]]['ip'],mes[request[0]]['port'])) #connect to peer
                client1.send(json.dumps(request)) # send request
                self.syncfiles(request,client1) #sync files



     

        self.msg = json.dumps({'port':self.port}) #send keep alive

        t = threading.Timer(5, self.sync)
        t.start()

    def syncfiles(self,request,client1):
        for names in request: #parse through requests

            getfiles = client1.recv(1024)
            download = open(names,"wb")
            while getfiles: # download file
                download.write(getfiles)
                getfiles = client1.recv(1024)


if __name__ == '__main__':
    #parse commmand line arguments
    parser = optparse.OptionParser(usage="%prog ServerIP ServerPort")
    options, args = parser.parse_args()
    if len(args) < 1:
        parser.error("No ServerIP and ServerPort")
    elif len(args) < 2:
        parser.error("No ServerIP or ServerPort")
    else:
        if validate_ip(args[0]) and validate_port(args[1]):
            tracker_ip = args[0]
            tracker_port = int(args[1])

        else:
            parser.error("Invalid ServerIP or ServerPort")

    #get the next available port
    synchronizer_port = get_next_avaliable_port(8000)
    synchronizer_thread = FileSynchronizer(tracker_ip,tracker_port,synchronizer_port)
    synchronizer_thread.start()
