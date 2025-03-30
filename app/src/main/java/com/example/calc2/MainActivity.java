package com.example.calc2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {



    public ArrayList<Character> ops;
    public ArrayList<Double> vals;
    public int op_count;
    public int dot_count;
    public Character op_char;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ops = new ArrayList<Character>(4);
        ops.add('+');
        ops.add('-');
        ops.add('*');
        ops.add('/');
        op_count=dot_count=0;
        op_char = null;

        vals=new ArrayList<Double>(16);


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void extend_number(char n)
    {
        EditText ndinp1 = (EditText)findViewById(R.id.text_display);
        String st = ndinp1.getText().toString();


        if(st.matches("") || st.charAt(st.length()-1) != '0')
        {
            st += n;
            ndinp1.setText(st);
        }

        else
        {
            boolean has_dot_before = false;


            int  last_dot_pos = st.lastIndexOf('.');

            int  last_op_pos = 0;
            if(op_char!=null)
            {
                last_op_pos = st.lastIndexOf(op_char);;
            }


            boolean cond1 = last_op_pos >= last_dot_pos && st.charAt(st.length()-1) != '0';
            boolean cond2 = last_op_pos < last_dot_pos;

            if(  cond1 || cond2)
            {
                st += n;
                ndinp1.setText(st);
            }
        }


    }

    public void extend_op(char n)
    {
        EditText ndinp1 = (EditText)findViewById(R.id.text_display);
        String st = ndinp1.getText().toString();


        if(!st.matches(""))
        {


            char last_char = st.charAt(st.length()-1);
            if(op_char==null &&  last_char != '.'  || ( op_char!=null&&op_char==n && last_char != '.' && !ops.contains(last_char) ) )
            {

                st += n;
                ndinp1.setText(st);

                if(op_char==null)
                {
                    op_char = n;
                }

                op_count+=1;
            }
        }




    }

    public void extend_dot()
    {
        EditText ndinp1 = (EditText)findViewById(R.id.text_display);
        String st = ndinp1.getText().toString();


        if(!st.matches(""))
        {

            boolean right_place = true;

            if(op_char!=null)
            {
                int op_pos =  st.lastIndexOf(op_char);

                int other_dots=0;

                for(int i=op_pos;i<st.length();i++)
                {
                    if(st.charAt(i)=='.')
                    {
                        other_dots+=1;
                        break;
                    }


                }

                if(other_dots > 0)
                {
                    right_place=false;
                }

            }

            char last_char = st.charAt(st.length()-1);
            if(right_place && (op_char==null || last_char !=op_char) && dot_count < op_count+1)
            {

                st += '.';
                ndinp1.setText(st);



                dot_count+=1;
            }
        }

    }

    public  void extend_zero()
    {
        EditText ndinp1 = (EditText)findViewById(R.id.text_display);
        String st = ndinp1.getText().toString();


        if(st.matches(""))
        {
            st += '0';
            ndinp1.setText(st);
        }

        else{
            boolean has_dot_before = false;


            int  last_dot_pos = st.lastIndexOf('.');

            int  last_op_pos = 0;
            if(op_char!=null)
            {
                last_op_pos = st.lastIndexOf(op_char);;
            }


            boolean cond1 = last_op_pos >= last_dot_pos && st.charAt(st.length()-1) != '0';
            boolean cond2 = last_op_pos < last_dot_pos;

            if(  cond1 || cond2)
            {
                st += '0';
                ndinp1.setText(st);
            }
        }


    }

    public void clear()
    {
        EditText ndinp1 = (EditText)findViewById(R.id.text_display);
        String st = ndinp1.getText().toString();
        ndinp1.setText("");
        op_count=dot_count=0;
        op_char = null;

    }


    public void remove_last()
    {
        EditText ndinp1 = (EditText)findViewById(R.id.text_display);
        String st = ndinp1.getText().toString();

        if(!st.matches(""))
        {
            char last_chr = st.charAt(st.length()-1);


            if(ops.contains(last_chr))
            {
                op_count-=1;
                if(op_count==0)
                {
                    op_char=null;
                }
            }

            if(last_chr=='.')
            {
                dot_count-=1;

                if(dot_count<0) {dot_count=0;}
            }


            st = st.substring(0,st.length()-1);
            ndinp1.setText(st);


        }
    }

    public void get_result()
    {
        EditText ndinp1 = (EditText)findViewById(R.id.text_display);
        String st = ndinp1.getText().toString();

        if(!st.matches(""))
        {

            if(op_char==null)
            {
                return;
            }


            String rg = "";
            rg+=op_char;

            Log.d("CALC",rg);
            String[] nums = st.split(Pattern.quote(rg));

            if(nums.length <= 1)
            {
                return;
            }

            for (String num : nums) {
                Log.d("CALC",num);
                vals.add(Double.parseDouble(num));
            }


            if(op_char=='/'&& vals.get(0)!=0.0 && vals.contains(0.0))
            {

                    vals.clear();
                    return;
            }

            Double total = 0.0;


            switch (op_char)
            {
                case ('+'):
                {
                    for(int a=0;a<vals.size();a++)
                    {
                        total += vals.get(a);
                    }
                    break;
                }

                case ('-'):
                {
                    total=vals.get(0);
                    for(int a=1;a<vals.size();a++)
                    {
                        total -= vals.get(a);
                    }
                    break;
                }

                case ('*'):
                {
                    total=1.0;
                    for(int a=0;a<vals.size();a++)
                    {
                        total *= vals.get(a);
                    }
                    break;
                }

                case ('/'):
                {
                    total=vals.get(0);
                    for(int a=1;a<vals.size();a++)
                    {
                        total /= vals.get(a);
                    }
                    break;
                }
            }

            ndinp1.setText(total.toString());
            op_count=0;
            dot_count=1;
            op_char=null;
            vals.clear();


        }
    }

    public void btn1(View view)
    {
        extend_number('1');
    }

    public void btn2(View view)
    {
        extend_number('2');
    }

    public void btn3(View view)
    {
        extend_number('3');
    }

    public void btn4(View view)
    {
        extend_number('4');
    }

    public void btn5(View view)
    {
        extend_number('5');
    }

    public void btn6(View view)
    {
        extend_number('6');
    }

    public void btn7(View view)
    {
        extend_number('7');
    }

    public void btn8(View view)
    {
        extend_number('8');
    }

    public void btn9(View view)
    {
        extend_number('9');
    }

    public void btnzero(View view)
    {
        extend_zero();
    }

    public void btndot(View view)
    {
        extend_dot();
    }

    public void btnclear(View view)
    {
        clear();
    }

    public void btnadd(View view)
    {
        extend_op('+');
    }

    public void btnsub(View view)
    {
        extend_op('-');
    }

    public void btnmul(View view)
    {
        extend_op('*');
    }

    public void btndiv(View view)
    {
        extend_op('/');
    }

    public void btndel(View view)
    {
        remove_last();
    }

    public void btnequal(View view)
    {
        get_result();
    }


}